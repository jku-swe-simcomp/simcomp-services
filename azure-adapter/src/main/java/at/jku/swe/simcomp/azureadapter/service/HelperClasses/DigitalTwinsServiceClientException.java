package at.jku.swe.simcomp.azureadapter.service.HelperClasses;

public class DigitalTwinsServiceClientException extends Exception {

    public DigitalTwinsServiceClientException() {
        super();
    }

    public DigitalTwinsServiceClientException(String message) {
        super(message);
    }

    public DigitalTwinsServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigitalTwinsServiceClientException(Throwable cause) {
        super(cause);
    }
}
