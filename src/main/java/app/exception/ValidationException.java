package app.exception;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
