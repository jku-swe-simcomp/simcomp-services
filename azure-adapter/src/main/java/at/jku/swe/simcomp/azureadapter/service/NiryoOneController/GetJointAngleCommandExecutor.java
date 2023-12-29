package at.jku.swe.simcomp.azureadapter.service.NiryoOneController;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;

public class GetJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.GrabCommand, ExecutionResultDTO> {
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.GrabCommand command, SimulationInstanceConfig config) throws Exception {
        return null;
    }
}
