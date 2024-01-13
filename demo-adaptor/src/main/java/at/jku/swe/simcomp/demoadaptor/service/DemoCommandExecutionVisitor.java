package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.JointPositionCommandExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static at.jku.swe.simcomp.demoadaptor.service.DemoSimulationInstanceService.currentJointPositions;

@Service
public class DemoCommandExecutionVisitor extends CommandExecutionVisitor {
    private final DemoSessionService demoSessionService;
    private final JointPositionCommandExecutor jointPositionCommandExecutor;
    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;
    public DemoCommandExecutionVisitor(DemoSessionService demoSessionService,
                                       JointPositionCommandExecutor jointPositionCommandExecutor,
                                       AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor){
        this.demoSessionService = demoSessionService;
        this.adjustJointAngleCommandExecutor = adjustJointAngleCommandExecutor;
        this.jointPositionCommandExecutor = jointPositionCommandExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException {
        return adjustJointAngleCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws SessionNotValidException {
        return jointPositionCommandExecutor.execute(command, demoSessionService.renewSession(sessionKey));
    }
}
