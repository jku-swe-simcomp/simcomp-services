package at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;

public class SetJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.SetJointPositionCommand, ExecutionResultDTO> {
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, SimulationInstanceConfig config) throws Exception {
        return null;
    }
}
