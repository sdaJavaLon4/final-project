package pl.javalon4.finalproject.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException() {
        super("Link not found");
    }
}
