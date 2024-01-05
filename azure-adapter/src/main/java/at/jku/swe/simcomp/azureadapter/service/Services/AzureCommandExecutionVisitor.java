package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.GetJointAngleCommandExecutor;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.SetJointAngleCommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import org.springframework.stereotype.Service;

@Service
public class AzureCommandExecutionVisitor extends CommandExecutionVisitor {

    private final AzureSessionService azureSessionService;

    private final SetJointAngleCommandExecutor setJointAngleCommandExecutor;

    private final GetJointAngleCommandExecutor getJointAngleCommandExecutor;

    public AzureCommandExecutionVisitor(AzureSessionService azureSessionService, SetJointAngleCommandExecutor setJointAngleCommandExecutor, GetJointAngleCommandExecutor getJointAngleCommandExecutor) {
        this.azureSessionService = azureSessionService;
        this.setJointAngleCommandExecutor = setJointAngleCommandExecutor;
        this.getJointAngleCommandExecutor = getJointAngleCommandExecutor;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws Exception {
        return setJointAngleCommandExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws Exception {
        return getJointAngleCommandExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }

}
