package revolusion.developers.hms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when a role is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleException extends RuntimeException{

    public RoleException(String message) {
        super(message);
    }



}
