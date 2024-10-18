package revolusion.developers.hms.service.impl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.*;
import revolusion.developers.hms.entity.status.OrderStatus;
import revolusion.developers.hms.entity.status.PaymentStatus;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.repository.*;
import revolusion.developers.hms.service.PaymentService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final UserPaymentRepository userPaymentRepository;
    private final HotelRepository hotelRepository;
    private final OrderRepository orderRepository;



    @Override
    public Page<PaymentDto> getAllPayments(int page, int size) {
        Page<Payment> paymentsPage = paymentRepository.findAll(PageRequest.of(page, size));
        return paymentsPage.map(this::paymentToDto);
    }

    @Override
    public Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));

        PaymentDto paymentDto = paymentToDto(payment);
        return Optional.ofNullable(paymentDto);
    }

    @Override
    @Transactional
    public PaymentDto createPayment(@Valid PaymentDto paymentDto) throws PaymentException {
        // 1. Convert dto to entity
        Payment payment = dtoToPayment(paymentDto);

        // 2. Validate the payment
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new PaymentException("Amount must be greater than zero.");
        }

        // 3. Check if the order associated with the payment exists
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            throw new PaymentException("Order is required for the payment.");
        }

        // 4. Checking the availability of the order
        Order order = orderRepository.findById(paymentDto.getOrderDto().getId())
                .orElseThrow(() -> new PaymentException("Buyurtma topilmadi."));

        // 5. Ensure the order status is pending
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new PaymentException("Order must be pending to create a payment.");
        }

        // 6. Set the order to Payment and paymentStatus
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // 7. Set the amount to match the total amount of the associated order
        payment.setAmount(order.getTotalAmount());

        // 8. Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // 9. Convert the saved Payment to DTO and return
        return paymentToDto(savedPayment);
    }



    @Override
    @Transactional
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException {
        // 1. Get the available payment
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));

        // 2. Get user details by order
        Order order = existingPayment.getOrder();
        if (order == null || order.getUser() == null) {
            throw new PaymentException("Foydalanuvchi yoki buyurtma topilmadi.");
        }
        User user = order.getUser();

        // 3. Get userPayment details
        UserPayment userPayment = userPaymentRepository.findByUserId(user.getId());
        if (userPayment == null) {
            existingPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(existingPayment);
            throw new PaymentException("Foydalanuvchi to'lov ma'lumotlari topilmadi.");
        }

        // 4. Get hotel details
        Hotel hotel = hotelRepository.findById(order.getRoom().getHotel().getId())
                .orElseThrow(() -> new PaymentException("Mehmonxona topilmadi."));

        // 5. Checking user balance
        if (userPayment.getBalance() < existingPayment.getAmount()) {
            existingPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(existingPayment);
            throw new PaymentException("Balance da yetarli mablag' mavjud emas.");
        }

        // 6. Update user balance
        userPayment.setBalance(userPayment.getBalance() - existingPayment.getAmount());
        userPaymentRepository.save(userPayment);

        // 7. Update hotel balance
        hotel.setBalance(hotel.getBalance() + existingPayment.getAmount());
        hotelRepository.save(hotel);

        // 8. Set updated payment amount and paymentMethod
        existingPayment.setAmount(order.getTotalAmount());
        existingPayment.setPaymentMethod(paymentDto.getPaymentMethod());

        // 9. Update payment status to PAID if successful
        existingPayment.setPaymentStatus(PaymentStatus.PAID);

        // 10. Save payment
        Payment updatedPayment = paymentRepository.save(existingPayment);

        // 11. Convert the saved Payment to DTO and return
        return paymentToDto(updatedPayment);
    }



    @Override
    public void deletePayment(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));
        paymentRepository.delete(payment);
    }


    private Payment dtoToPayment(PaymentDto paymentDto) {
        return modelMapper.map(paymentDto, Payment.class);
    }


    public PaymentDto paymentToDto(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

}
