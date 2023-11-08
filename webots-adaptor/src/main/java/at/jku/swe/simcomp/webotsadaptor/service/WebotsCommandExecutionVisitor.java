package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutionVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.command_executors.*;

public class WebotsCommandExecutionVisitor extends CommandExecutionVisitor {
    private final WebotsSessionService demoSessionService;
    private final PoseCommandExecutor poseCommandExecutor;
    private final GrabExecutor grabExecutor;
    private final OpenHandExecutor openHandExecutor;
    private final ResetToHomeExecutor resetToHomeExecutor;
    private final SetJointExecutor setJointExecutor;
    private final AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor;
    public WebotsCommandExecutionVisitor(WebotsSessionService demoSessionService,
                                         PoseCommandExecutor poseCommandExecutor,
                                         AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor,
                                         GrabExecutor grabExecutor,
                                         OpenHandExecutor openHandExecutor,
                                         ResetToHomeExecutor resetToHomeExecutor,
                                         SetJointExecutor setJointExecutor){
        this.demoSessionService = demoSessionService;
        this.adjustJointAnglesCommandExecutor = adjustJointAnglesCommandExecutor;
        this.poseCommandExecutor = poseCommandExecutor;
        this.grabExecutor = grabExecutor;
        this.openHandExecutor = openHandExecutor;
        this.resetToHomeExecutor = resetToHomeExecutor;
        this.setJointExecutor = setJointExecutor;
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
