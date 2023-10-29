package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutionVisitor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.AdjustJointAnglesCommandExecutor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.PoseCommandExecutor;
import org.springframework.stereotype.Service;

@Service
public class DemoCommandExecutionVisitor extends CommandExecutionVisitor {
    private final DemoSessionService demoSessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor;
    public DemoCommandExecutionVisitor(DemoSessionService demoSessionService,
                                       PoseCommandExecutor poseCommandExecutor,
                                       AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor){
        this.demoSessionService = demoSessionService;
        this.adjustJointAnglesCommandExecutor = adjustJointAnglesCommandExecutor;
        this.poseCommandExecutor = poseCommandExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAnglesCommand command, String sessionKey) throws SessionNotValidException {
        return adjustJointAnglesCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws SessionNotValidException {
        return poseCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }
}
