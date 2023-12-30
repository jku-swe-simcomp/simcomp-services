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

@Service
public class WebotsCommandExecutionVisitor extends CommandExecutionVisitor {
    private final SessionService sessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;
    private final GrabExecutor grabExecutor;
    private final OpenHandExecutor openHandExecutor;
    private final ResetToHomeExecutor resetToHomeExecutor;
    private final SetJointExecutor setJointExecutor;
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

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return adjustJointAngleCommandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return poseCommandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.GrabCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return grabExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.OpenHandCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return openHandExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.ResetToHomeCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return resetToHomeExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return setJointExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
}
