package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.PoseCommandExecutor;
import org.springframework.stereotype.Service;

@Service
public class DemoCommandExecutionVisitor extends CommandExecutionVisitor {
    private final DemoSessionService demoSessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;
    public DemoCommandExecutionVisitor(DemoSessionService demoSessionService,
                                       PoseCommandExecutor poseCommandExecutor,
                                       AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor){
        this.demoSessionService = demoSessionService;
        this.adjustJointAngleCommandExecutor = adjustJointAngleCommandExecutor;
        this.poseCommandExecutor = poseCommandExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException {
        return adjustJointAngleCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws SessionNotValidException {
        return poseCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }
}
