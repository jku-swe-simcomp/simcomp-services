package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;

public class RoboOperationFailedException extends Exception {
    private final RoboStateDTO state;
    public RoboOperationFailedException(String message) {
       this(message, null);
    }

    public RoboOperationFailedException(String message, RoboStateDTO state) {
        super(message);
        this.state = state;
    }

    public RoboStateDTO getState(){
        return state;
    }
}
