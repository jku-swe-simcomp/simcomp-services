package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.command_executors.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.webotsadaptor.service.command_executors.PoseCommandExecutor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebotsCommandExecutionVisitor extends CommandExecutionVisitor {
    private final WebotsSessionService demoSessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;
    public WebotsCommandExecutionVisitor(WebotsSessionService demoSessionService,
                                       PoseCommandExecutor poseCommandExecutor,
                                       AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor){
        this.demoSessionService = demoSessionService;
        this.adjustJointAngleCommandExecutor = adjustJointAngleCommandExecutor;
        this.poseCommandExecutor = poseCommandExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return adjustJointAngleCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        return poseCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }
}
