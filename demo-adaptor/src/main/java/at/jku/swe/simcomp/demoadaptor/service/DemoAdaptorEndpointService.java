package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.*;
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
           case POSE -> executePose(command.viewAsPoseCommand(), sessionId);
           case SET_ORIENTATION -> executeSetOrientation(command.viewAsSetOrientationCommand(), sessionId);
           case SET_POSITION -> executeSetPosition(command.viewAsSetPositionCommand(), sessionId);
           case ADJUST_JOINT_ANGLE -> executeAdjustJointAngle(command.viewAsAdjustJointAngleCommand(), sessionId);
           case SET_JOINT_POSITIONS -> executeSetJointPositions(command.viewAsSetJointPositionCommand(), sessionId);
           case SET_SPEED -> executeSetSpeed(command.viewAsSetSpeedCommand(), sessionId);
           case DEFAULT -> executeDefault(sessionId);
           case GRAB -> executeGrab(sessionId);
           case OPEN_HAND -> executeOpenHand(sessionId);
           case PAUSE -> executePause(sessionId);
           case RESUME -> executeResume(sessionId);
           case RESET_TO_HOME -> executeResetToHome(sessionId);
           case STOP -> executeStop(command, sessionId);
           case TOGGLE_GRIPPER_MODE -> executeToggleGripperMode(sessionId);
           default -> throw new UnsupportedOperationException("Unsupported action type: " + command.getActionType());
        };
    }


    // private region
    private ExecutionResultDTO executeSetPosition(SetPositionCommand setPositionCommand, String sessionId) {
        return getDemoResult("Set position");
    }

    private ExecutionResultDTO executeToggleGripperMode(String sessionId) {
        return getDemoResult("Toggled gripper mode");
    }

    private ExecutionResultDTO executeStop(ExecutionCommandDTO command, String sessionId) {
        return getDemoResult("Stopped");
    }

    private ExecutionResultDTO executeSetOrientation(SetOrientationCommand command, String sessionId) {
        return getDemoResult("Set orientation");
    }

    private ExecutionResultDTO executeResetToHome(String sessionId) {
        return getDemoResult("Reset to home");
    }

    private ExecutionResultDTO executeResume(String sessionId) {
        return getDemoResult("Resumed");
    }

    private ExecutionResultDTO executePause(String sessionId) {
        return getDemoResult("Paused");
    }

    private ExecutionResultDTO executeSetSpeed(SetSpeedCommand command, String sessionId) {
        return getDemoResult("Set speed");
    }

    private ExecutionResultDTO executeSetJointPositions(SetJointPositionCommand command, String sessionId) {
        return getDemoResult("Set joint positions");
    }

    private ExecutionResultDTO executeAdjustJointAngle(AdjustJointAngleCommand command, String sessionId) {
        return getDemoResult("Adjusted joint angle");
    }

    private ExecutionResultDTO executeOpenHand(String sessionId) {
        return getDemoResult("Opened hand");
    }

    private ExecutionResultDTO executeGrab(String sessionId) {
        return getDemoResult("Grabbed");
    }

    private ExecutionResultDTO executeDefault(String sessionId) {
        return getDemoResult("Executed default action");
    }

    private ExecutionResultDTO executePose(PoseCommand command, String sessionId) {
        return getDemoResult("Executed pose");
    }

    private ExecutionResultDTO getDemoResult(String message){
        return ExecutionResultDTO.builder()
                .message(message)
                .success(true)
                .build();
    }
}
