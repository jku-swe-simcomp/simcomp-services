package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;

@Service
public class AdjustJointAnglesCommandExecutor
        implements CommandExecutor<ExecutionCommand.AdjustJointAnglesCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @SneakyThrows
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAnglesCommand command, WebotsSimulationConfig config) {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        JointAngleAdjustmentDTO movement = command.jointAngleAdjustments().get(0); // now a list is used, maybe change to only send one angle adjustment per command
        json.put("operation", "adjust_axis");
        json.put("axis", movement.getJoint());
        json.put("value", movement.getByDegree());
        return WebotsExecutionService.executeCommand(json, config);
    }
}
