package revolusion.developers.hms.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import revolusion.developers.hms.entity.Order;
import revolusion.developers.hms.entity.Payment;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.OrderDto;
import revolusion.developers.hms.payload.PaymentDto;
import revolusion.developers.hms.repository.PaymentRepository;
import revolusion.developers.hms.repository.RoomRepository;
import revolusion.developers.hms.service.PaymentService;

import java.util.List;
import java.util.Optional;

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
        return null;
    }

    @Override
    public Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException {
        return Optional.empty();
    }

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) throws PaymentException {
        return null;
    }

    @Override
    public PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public void deletePayment(Long paymentId) throws ResourceNotFoundException {

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
