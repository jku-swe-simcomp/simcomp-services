package at.jku.swe.simcomp.azureadapter.service.HelperClasses;

/**
 * The DigitalTwinsServiceClientException class represents an exception specific to the Digital Twins service client.
 * This exception can be thrown to indicate errors or unexpected situations related to the Digital Twins service.
 */
public class DigitalTwinsServiceClientException extends Exception {

    /**
     * Constructs a DigitalTwinsServiceClientException with no specified detail message.
     */
    public DigitalTwinsServiceClientException() {
        super();
    }

    /**
     * Constructs a DigitalTwinsServiceClientException with the specified detail message.
     *
     * @param message The detail message for the exception.
     */
    public DigitalTwinsServiceClientException(String message) {
        super(message);
    }

    /**
     * Constructs a DigitalTwinsServiceClientException with the specified detail message and cause.
     *
     * @param message The detail message for the exception.
     * @param cause   The cause of the exception (can be another exception that caused this one).
     */
    public DigitalTwinsServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a DigitalTwinsServiceClientException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause The cause of the exception (can be another exception that caused this one).
     */
    public DigitalTwinsServiceClientException(Throwable cause) {
        super(cause);
    }
}
