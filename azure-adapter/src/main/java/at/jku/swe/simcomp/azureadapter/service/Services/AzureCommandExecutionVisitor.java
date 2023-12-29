package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.NiryoOneController;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ComponentScan("at.jku.swe.simcomp.azureadapter.service.NiryoOneController")
public class AzureCommandExecutionVisitor extends CommandExecutionVisitor {
    /*
     * TODO: Initialize all the needed services
     */
    private final NiryoOneController niryoOneController;
    public static final List<Double> currentJointPositions = new ArrayList<>(List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
    private final AzureSessionService azureSessionService;

    public AzureCommandExecutionVisitor(NiryoOneController niryoOneController, AzureSessionService demoSessionService){
        this.niryoOneController = niryoOneController;
        this.azureSessionService = demoSessionService;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.AdjustJointAngleCommand command, String sessionKey) throws SessionNotValidException {
        return null;
    }

    @Override
    public ExecutionResultDTO visit(ExecutionCommand.SetJointPositionCommand command, String sessionKey) throws SessionNotValidException {
        return null;
    }
}
