package revolusion.developers.hms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception thrown when a room is invalid.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoomException extends RuntimeException{

    public RoomException(String message) {
        super(message);
    }



}
