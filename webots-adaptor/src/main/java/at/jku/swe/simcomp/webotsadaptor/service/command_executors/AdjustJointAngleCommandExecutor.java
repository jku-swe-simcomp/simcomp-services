package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.SneakyThrows;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class AdjustJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, WebotsSimulationConfig config) throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        JointAngleAdjustmentDTO movement = command.jointAngleAdjustment(); // now a list is used, maybe change to only send one angle adjustment per command
        json.put("operation", "set_axis");
        json.put("axis", movement.getJoint().getIndex());
        json.put("value", movement.getByRadians());
        return WebotsExecutionService.executeCommand(json, config);
    }
}
