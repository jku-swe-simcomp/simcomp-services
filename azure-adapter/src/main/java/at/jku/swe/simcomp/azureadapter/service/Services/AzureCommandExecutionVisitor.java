package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.SetJointAngleCommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import org.springframework.stereotype.Service;

/**
 * The AzureCommandExecutionVisitor class extends the CommandExecutionVisitor to provide
 * specific execution logic for Azure-related execution commands in the context of a session.
 */
@Service
public class AzureCommandExecutionVisitor extends CommandExecutionVisitor {

    private final AzureSessionService azureSessionService;

    private final SetJointAngleCommandExecutor setJointAngleCommandExecutor;

    private final AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;

    /**
     * Constructs an AzureCommandExecutionVisitor with the specified dependencies.
     *
     * @param azureSessionService          The AzureSessionService responsible for managing Azure sessions.
     * @param setJointAngleCommandExecutor The command executor for SetJointPositionCommand.
     * @param adjustJointAngleCommandExecutor The command executor for AdjustJointAngleCommand.
     */
    public AzureCommandExecutionVisitor(AzureSessionService azureSessionService,
                                        SetJointAngleCommandExecutor setJointAngleCommandExecutor,
                                        AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor) {
        this.azureSessionService = azureSessionService;
        this.setJointAngleCommandExecutor = setJointAngleCommandExecutor;
        this.adjustJointAngleCommandExecutor = adjustJointAngleCommandExecutor;
    }

    /**
     * Executes the SetJointPositionCommand in the context of a session.
     *
     * @param command   The SetJointPositionCommand to be executed.
     * @param sessionKey The key of the session in which the command is executed.
     * @return The result of the command execution.
     * @throws Exception If an error occurs during command execution.
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws Exception {
        return setJointAngleCommandExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }

    /**
     * Executes the AdjustJointAngleCommand in the context of a session.
     *
     * @param command   The AdjustJointAngleCommand to be executed.
     * @param sessionKey The key of the session in which the command is executed.
     * @return The result of the command execution.
     * @throws Exception If an error occurs during command execution.
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws Exception {
        return adjustJointAngleCommandExecutor.execute(command, azureSessionService.renewSession(sessionKey));
    }
}