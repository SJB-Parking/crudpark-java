package app.exception;

/**
 * Exception thrown when business rule is violated
 */
public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }
}
