package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import lombok.Getter;

/**
 * This exception is thrown when a robo operation failed.
 */
@Getter
public class RoboOperationFailedException extends Exception {
    /**
     * The state of the robo after the operation failed.
     */
    private final RoboStateDTO state;

    /**
     * Constructor.
     * @param message The message of the exception.
     */
    public RoboOperationFailedException(String message) {
       this(message, null);
    }

    /**
     * Constructor.
     * @param message The message of the exception.
     * @param state The state of the robo after the operation failed.
     */
    public RoboOperationFailedException(String message, RoboStateDTO state) {
        super(message);
        this.state = state;
    }

}
