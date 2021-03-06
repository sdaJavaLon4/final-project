package pl.javalon4.finalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private String incorrectPasswordHandler(IncorrectPasswordException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String linkNotFoundException(LinkNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String linkNotFoundException(CategoryNotFoundException ex) {
        return ex.getMessage();
    }
}
