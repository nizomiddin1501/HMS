package revolusion.developers.hms.service.impl;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Payment;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.repository.PaymentRepository;
import revolusion.developers.hms.service.PaymentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(ModelMapper modelMapper, PaymentRepository paymentRepository) {
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
    }


    @Override
    public List<PaymentDto> getAllPayments(int page, int size) {
        List<Payment> payments = paymentRepository.findAll(PageRequest.of(page, size)).getContent();
        return payments.stream()
                .map(this::paymentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));

        // Convert Payment entity to PaymentDto
        PaymentDto paymentDto = paymentToDto(payment);
        return Optional.ofNullable(paymentDto);
    }

    @Override
    @Transactional
    public PaymentDto createPayment(@Valid PaymentDto paymentDto) throws PaymentException {
        // Convert dto to entity
        Payment payment = dtoToPayment(paymentDto);

        // Validate the payment DTO
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new PaymentException("Amount must be greater than zero.");
        }

        // Check if the order associated with the payment exists
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            throw new PaymentException("Order is required for the payment.");
        }

        // Save the payment
        Payment savedPayment = paymentRepository.save(payment);

        // Convert saved Payment entity back to PaymentDto and return
        return paymentToDto(savedPayment);
    }



    @Override
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));

        // update payment details
        existingPayment.setAmount(paymentDto.getAmount());
        existingPayment.setPaymentMethod(paymentDto.getPaymentMethod());

        // Save updated payment
        Payment updatedPayment = paymentRepository.save(existingPayment);

        // Convert updated payment entity to DTO and return
        return paymentToDto(updatedPayment);
    }



    @Override
    public void deletePayment(Long paymentId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", " Id ", paymentId));
        paymentRepository.delete(payment);
    }

    // DTO ---> Entity
    private Payment dtoToPayment(PaymentDto paymentDto) {
        return modelMapper.map(paymentDto, Payment.class);
    }

    // Entity ---> DTO
    public PaymentDto paymentToDto(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

}
