package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.PaymentDto;


import java.util.List;
import java.util.Optional;

public interface PaymentService {

    Page<PaymentDto> getAllPayments(int page, int size);

    Optional<PaymentDto> getPaymentById(Long paymentId);

    PaymentDto createPayment(PaymentDto paymentDto);

    PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto);

    void deletePayment(Long paymentId);








}
