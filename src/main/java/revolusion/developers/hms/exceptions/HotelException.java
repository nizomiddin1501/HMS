package revolusion.developers.hms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when a user is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HotelException extends RuntimeException{

    public HotelException(String message) {
        super(message);
    }



}
