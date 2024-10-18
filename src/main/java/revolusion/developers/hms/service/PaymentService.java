package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.PaymentDto;


import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Page<PaymentDto> getAllPayments(int page, int size);

    Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException;

    PaymentDto createPayment(PaymentDto paymentDto) throws PaymentException;

    PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException;

    void deletePayment(Long paymentId) throws ResourceNotFoundException;








}
