package revolusion.developers.hms.exceptions.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import revolusion.developers.hms.exceptions.ResourceNotFoundException;
import revolusion.developers.hms.payload.CustomApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String message = ex.getMessage();
        CustomApiResponse apiResponse = new CustomApiResponse(message,false,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }



//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
//        Map<String,String > resp = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error -> {
//            String fieldName = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            resp.put(fieldName,message);
//        }));
//        return new ResponseEntity<Map<String,String>>(resp, HttpStatus.BAD_REQUEST);
//    }








}
