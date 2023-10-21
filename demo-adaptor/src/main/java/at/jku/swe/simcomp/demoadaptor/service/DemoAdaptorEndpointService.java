package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoAdaptorEndpointService implements AdaptorEndpointService {
    private static final String DEMO_SESSION_SINGLETON_ID = "1234";

    @Override
    public String initSession() throws SessionInitializationFailedException {
        return DEMO_SESSION_SINGLETON_ID;
    }

    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        if(!sessionId.equals(DEMO_SESSION_SINGLETON_ID))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionId));
        // close session
    }

    @Override
    public String getAttributeValue(String attributeName, String sessionId) throws SessionNotValidException {
        if(!sessionId.equals(DEMO_SESSION_SINGLETON_ID))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionId));
        return "42";
    }

    @Override
    public ExecutionResultDTO executeSequence(List<ExecutionCommandDTO> executionCommands, String sessionId) throws SessionNotValidException {
        if(!sessionId.equals(DEMO_SESSION_SINGLETON_ID))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionId));

        for(ExecutionCommandDTO command : executionCommands){
            executeAction(command, sessionId);
        }
        return getDemoResult("Executed sequence");
    }

    @Override
    public ExecutionResultDTO executeAction(ExecutionCommandDTO command, String sessionId) throws SessionNotValidException {
        if(!sessionId.equals(DEMO_SESSION_SINGLETON_ID))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionId));

        return switch(command.getActionType()){
           case POSE -> executePose(command, sessionId);
           case DEFAULT -> executeDefault(command, sessionId);
           case GRAB -> executeGrab(command, sessionId);
           case OPEN_HAND -> executeOpenHand(command, sessionId);
           case ADJUST_JOINT_ANGLE -> executeAdjustJointAngle(command, sessionId);
           case SET_JOINT_POSITIONS -> executeSetJointPositions(command, sessionId);
           case SET_SPEED -> executeSetSpeed(command, sessionId);
           case PAUSE -> executePause(command, sessionId);
           case RESUME -> executeResume(command, sessionId);
           case RESET_TO_HOME -> executeResetToHome(command, sessionId);
           case SET_ORIENTATION -> executeSetOrientation(command, sessionId);
           case STOP -> executeStop(command, sessionId);
           case TOGGLE_GRIPPER_MODE -> executeToggleGripperMode(command, sessionId);
           default -> throw new UnsupportedOperationException("Unsupported action type: " + command.getActionType());
        };
    }

    private ExecutionResultDTO executeToggleGripperMode(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Toggled gripper mode");
    }

    private ExecutionResultDTO executeStop(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Stopped");
    }

    private ExecutionResultDTO executeSetOrientation(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Set orientation");
    }

    private ExecutionResultDTO executeResetToHome(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Reset to home");
    }

    private ExecutionResultDTO executeResume(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Resumed");
    }

    private ExecutionResultDTO executePause(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Paused");
    }

    private ExecutionResultDTO executeSetSpeed(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Set speed");
    }

    private ExecutionResultDTO executeSetJointPositions(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Set joint positions");
    }

    private ExecutionResultDTO executeAdjustJointAngle(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Adjusted joint angle");
    }

    private ExecutionResultDTO executeOpenHand(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Opened hand");
    }

    private ExecutionResultDTO executeGrab(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Grabbed");
    }

    private ExecutionResultDTO executeDefault(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Executed default action");
    }

    private ExecutionResultDTO executePose(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Executed pose");
    }

    private ExecutionResultDTO getDemoResult(String message){
        return ExecutionResultDTO.builder()
                .message(message)
                .success(true)
                .build();
    }
}
