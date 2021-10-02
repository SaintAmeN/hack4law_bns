package pl.hack4law.lawrules.exception;

public class Unauthenticated extends RuntimeException {
    public Unauthenticated(String msg) {
        super(msg);
    }
}
