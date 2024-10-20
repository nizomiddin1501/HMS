package revolusion.developers.hms.service.impl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.*;
import revolusion.developers.hms.entity.enums.OrderStatus;
import revolusion.developers.hms.entity.enums.PaymentStatus;
import revolusion.developers.hms.entity.enums.RoomStatus;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.mapper.PaymentMapper;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.repository.*;
import revolusion.developers.hms.service.PaymentService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserPaymentRepository userPaymentRepository;
    private final HotelRepository hotelRepository;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;
    private final PaymentMapper paymentMapper;



    @Override
    public Page<PaymentDto> getAllPayments(int page, int size) {
        Page<Payment> paymentsPage = paymentRepository.findAll(PageRequest.of(page, size));
        return paymentsPage.map(paymentMapper::paymentToDto);
    }

    @Override
    public Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));
        return Optional.of(paymentMapper.paymentToDto(payment));
    }

    @Override
    @Transactional
    public PaymentDto createPayment(@Valid PaymentDto paymentDto) throws PaymentException {
        Payment payment = paymentMapper.dtoToPayment(paymentDto);
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new PaymentException("Amount must be greater than zero.");
        }
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            throw new PaymentException("Order is required for the payment.");
        }
        Order order = orderRepository.findById(paymentDto.getOrderDto().getId())
                .orElseThrow(() -> new PaymentException("Buyurtma topilmadi."));

        payment.setPaymentDate(Date.valueOf(LocalDate.now()));

        if (paymentDto.getPaymentStatus() == PaymentStatus.PAID) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.CONFIRMED);

            Room room = order.getRoom();
            room.setRoomStatus(RoomStatus.BOOKED);
            roomRepository.save(room);
        } else if (paymentDto.getPaymentStatus() == PaymentStatus.FAILED) {
            throw new PaymentException("Payment failed.");
        }

        Payment savedPayment = paymentRepository.save(payment);
        orderRepository.save(order);
        return paymentMapper.paymentToDto(savedPayment);
    }



    @Override
    @Transactional
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));

        Order order = existingPayment.getOrder();
        if (order == null || order.getUser() == null) {
            throw new PaymentException("Order not found for the payment.");
        }

        User user = order.getUser();
        UserPayment userPayment = userPaymentRepository.findByUserId(user.getId());
        if (userPayment == null) {
            existingPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(existingPayment);
            throw new PaymentException("Foydalanuvchi to'lov ma'lumotlari topilmadi.");
        }

        Hotel hotel = hotelRepository.findById(order.getRoom().getHotel().getId())
                .orElseThrow(() -> new PaymentException("Mehmonxona topilmadi."));
        if (userPayment.getBalance() < existingPayment.getAmount()) {
            existingPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(existingPayment);
            throw new PaymentException("Balance da yetarli mablag' mavjud emas.");
        }

        Room room = order.getRoom();
        if (room.getRoomStatus() != RoomStatus.BOOKED) {
            room.setRoomStatus(RoomStatus.BOOKED);
            roomRepository.save(room);
        }

        if (order.getDeadline() != null && order.getDeadline().isBefore(LocalDateTime.now())) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            room.setRoomStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
            orderRepository.save(order);
            existingPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(existingPayment);
            throw new PaymentException("Orderning amal qilish muddati tugagan.");
        }

        userPayment.setBalance(userPayment.getBalance() - existingPayment.getAmount());
        userPaymentRepository.save(userPayment);

        hotel.setBalance(hotel.getBalance() + existingPayment.getAmount());
        hotelRepository.save(hotel);

        existingPayment.setAmount(order.getTotalAmount());
        existingPayment.setPaymentMethod(paymentDto.getPaymentMethod());
        existingPayment.setPaymentStatus(PaymentStatus.PAID);
        existingPayment.setPaymentDate(Date.valueOf(LocalDate.now()));

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return paymentMapper.paymentToDto(updatedPayment);
    }



    @Override
    public void deletePayment(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));
        paymentRepository.delete(payment);
    }
}
