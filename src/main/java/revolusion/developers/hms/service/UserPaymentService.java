package revolusion.developers.hms.service;

import org.springframework.data.domain.Page;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.exceptions.UserPaymentException;
import revolusion.developers.hms.payload.UserPaymentDto;
import java.util.Optional;

public interface UserPaymentService {



    // get all userPayments using pagination
    Page<UserPaymentDto> getAllUserPayments(int page, int size);

    // get userPayment by ID
    Optional<UserPaymentDto> getUserPaymentById(Long userPaymentId) throws ResourceNotFoundException;

    // create a new userPayment
    UserPaymentDto createUserPayment(UserPaymentDto userPaymentDto) throws UserPaymentException;

    // update an existing userPayment
    UserPaymentDto updateUserPayment(Long userPaymentId, UserPaymentDto userPaymentDto) throws ResourceNotFoundException;

    // delete a userPayment
    void deleteUserPayment(Long userPaymentId) throws ResourceNotFoundException;






}
