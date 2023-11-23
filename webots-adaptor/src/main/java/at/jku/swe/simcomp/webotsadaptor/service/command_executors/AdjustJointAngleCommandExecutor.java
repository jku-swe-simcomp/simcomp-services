package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;

import java.io.IOException;

@Service
public class AdjustJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getInstanceHost() + " on port " + config.getInstancePort());

        JSONObject json = new JSONObject();
        JointAngleAdjustmentDTO movement = command.jointAngleAdjustment(); // now a list is used, maybe change to only send one angle adjustment per command
        json.put("operation", "set_axis");
        json.put("axis", movement.getJoint().getIndex());
        json.put("value", movement.getByRadians());
        return WebotsExecutionService.executeCommand(json, config);
    }
}
