package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.command_executors.*;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Class to implement the visitor pattern for the supported
 * command types. For each command type a visitor method is
 * implemented, that executes the command with the respective
 * command executor.
 */
@Service
public class WebotsCommandExecutionVisitor extends CommandExecutionVisitor {
    private final SessionService sessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;
    private final GrabExecutor grabExecutor;
    private final OpenHandExecutor openHandExecutor;
    private final ResetToHomeExecutor resetToHomeExecutor;
    private final SetJointExecutor setJointExecutor;

    /**
     * Constructor to set all command executors, and the session service
     * @param sessionService session service instance to keep track of the session instances
     * @param poseCommandExecutor executor for pose command
     * @param adjustJointAngleCommandExecutor executor of joint adjustment command
     * @param grabExecutor executor of grab command
     * @param openHandExecutor executor of open hand command
     * @param resetToHomeExecutor executor of reset to home command
     * @param setJointExecutor executor of joint set command
     */
    public WebotsCommandExecutionVisitor(SessionService sessionService,
                                         PoseCommandExecutor poseCommandExecutor,
                                         AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor,
                                         GrabExecutor grabExecutor,
                                         OpenHandExecutor openHandExecutor,
                                         ResetToHomeExecutor resetToHomeExecutor,
                                         SetJointExecutor setJointExecutor){
        this.sessionService = sessionService;
        this.adjustJointAngleCommandExecutor = adjustJointAngleCommandExecutor;
        this.poseCommandExecutor = poseCommandExecutor;
        this.grabExecutor = grabExecutor;
        this.openHandExecutor = openHandExecutor;
        this.setJointExecutor = setJointExecutor;
        this.resetToHomeExecutor = resetToHomeExecutor;
    }

    /**
     * Method to call the executor for adjust joint angle commands, with a new command.
     * The execution is executed for a session, the session get renewed.
     *
     * @param command to adjust a joint angle for the robot arm
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return adjustJointAngleCommandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    /**
     * Method to call the executor for pose commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to query the axis values of the robot
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return poseCommandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    /**
     * Method to call the executor for grab commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to grab the robot simulation arm
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.GrabCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return grabExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    /**
     * Method to call the executor for open hand commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to open the hand of the robot simulation arm
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.OpenHandCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return openHandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    /**
     * Method to call the executor for reste to commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to set the robot arm to the home position
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.ResetToHomeCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return resetToHomeExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    /**
     * Method to call the executor for set joint position commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to set the joint poistion of one axis of the robot arm
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return setJointExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
}
