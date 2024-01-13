package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.*;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import org.springframework.stereotype.Service;

@Service
public class AzureCommandExecutionVisitor extends CommandExecutionVisitor {

    private final AzureSessionService azureSessionService;

    private final SetJointAngleCommandExecutor setJointAngleCommandExecutor;

    private final AdjustJointAngleExecutor adjustJointAngleExecutor;


    public AzureCommandExecutionVisitor(AzureSessionService azureSessionService,
                                        SetJointAngleCommandExecutor setJointAngleCommandExecutor,
                                        AdjustJointAngleExecutor adjustJointAngleExecutor) {
        this.azureSessionService = azureSessionService;
        this.setJointAngleCommandExecutor = setJointAngleCommandExecutor;
        this.adjustJointAngleExecutor = adjustJointAngleExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws Exception {
        return setJointAngleCommandExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws Exception {
        return adjustJointAngleExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }
}
