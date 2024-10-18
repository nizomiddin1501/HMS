package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserPaymentException;
import revolusion.developers.hms.payload.UserPaymentDto;
import java.util.Optional;

public interface UserPaymentService {


    Page<UserPaymentDto> getAllUserPayments(int page, int size);

    Optional<UserPaymentDto> getUserPaymentById(Long userPaymentId) throws ResourceNotFoundException;

    UserPaymentDto createUserPayment(UserPaymentDto userPaymentDto) throws UserPaymentException;

    UserPaymentDto updateUserPayment(Long userPaymentId, UserPaymentDto userPaymentDto) throws ResourceNotFoundException;

    void deleteUserPayment(Long userPaymentId) throws ResourceNotFoundException;






}
