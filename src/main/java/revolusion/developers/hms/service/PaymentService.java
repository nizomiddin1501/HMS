package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.PaymentException;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.PaymentDto;


import java.util.List;
import java.util.Optional;

public interface PaymentService {

    // get all payments using pagination
    Page<PaymentDto> getAllPayments(int page, int size);

    // get payment by ID
    Optional<PaymentDto> getPaymentById(Long paymentId) throws ResourceNotFoundException;

    // create a new payment
    PaymentDto createPayment(PaymentDto paymentDto) throws PaymentException;

    // update an existing payment
    PaymentDto updatePayment(Long paymentId, PaymentDto paymentDto) throws ResourceNotFoundException;

    // delete a payment
    void deletePayment(Long paymentId) throws ResourceNotFoundException;








}
