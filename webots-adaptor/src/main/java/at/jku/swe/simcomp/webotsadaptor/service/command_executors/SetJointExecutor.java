package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SetJointExecutor
        implements CommandExecutor<ExecutionCommand.SetJointPositionCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, WebotsSimulationConfig config) throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        @NonNull JointPositionDTO movement = command.jointPosition(); // now a list is used, maybe change to only send one angle adjustment per command
        json.put("operation", "set_axis");
        json.put("axis", movement.getJoint().getIndex());
        json.put("value", movement.getRadians());
        return WebotsExecutionService.executeCommand(json, config);
    }
}


