package accountmanager.supporttool.advice;

import accountmanager.supporttool.http.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AMSTExceptionHandler {

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<ResponseMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        String errorMessage = "the role requested is not found .. please choose a suitable user role";
//        ResponseMessage responseMessage = new ResponseMessage();
//        responseMessage.setMessage(errorMessage);
//        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ResponseMessage> InsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        String errorMessage = "You're blocked .. you can't login to this app";
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(errorMessage);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseMessage> BadCredentialsException(BadCredentialsException ex) {
        String errorMessage = "email or password is wrong ... please check your credentials again";
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(errorMessage);
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}