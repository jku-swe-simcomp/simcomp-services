package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

public class SetJointExecutor
        implements CommandExecutor<ExecutionCommand.SetJointPositionsCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @SneakyThrows
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionsCommand command, WebotsSimulationConfig config) {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        JointPositionDTO movement = command.jointPosition().get(0);
        json.put("operation", "set_axis");
        json.put("axis", movement.getJoint());
        json.put("value", movement.getDegree());
        return WebotsExecutionService.executeCommand(json, config);
    }
}
