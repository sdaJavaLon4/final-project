package pl.javalon4.finalproject.exception;


public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super(login + " not found");
    }
}
