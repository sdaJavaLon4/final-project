package pl.javalon4.finalproject.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Wrong password");
    }
}
