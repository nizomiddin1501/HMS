package revolusion.developers.hms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when a userPayment is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserPaymentException extends RuntimeException{

    public UserPaymentException(String message) {
        super(message);
    }



}
