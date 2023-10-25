package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

public class CompositeCommandExecutionFailedException extends Exception {
    private final Exception originalException;
    public CompositeCommandExecutionFailedException(String message, Exception originalException) {
        super(message);
        this.originalException=originalException;
    }

    public Exception getOriginalException() {
        return originalException;
    }
}
