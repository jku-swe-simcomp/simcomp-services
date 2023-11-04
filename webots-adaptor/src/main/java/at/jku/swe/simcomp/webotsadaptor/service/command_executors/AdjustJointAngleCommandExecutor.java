package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
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

    @SneakyThrows
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, WebotsSimulationConfig config) {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());
        Socket client;
        DataOutputStream out;
        try {
            client = new Socket(config.getSimulationEndpointUrl(), config.getSimulationPort());
            out = new DataOutputStream(client.getOutputStream());
            System.out.println("Connected to " + client.getRemoteSocketAddress());

            JSONObject json = new JSONObject();
            JointAngleAdjustmentDTO movement = command.jointAngleAdjustment(); // now a list is used, maybe change to only send one angle adjustment per command
            json.put("operation", "adjust_axis");
            json.put("axis", movement.getJoint().getIndex());
            json.put("value", movement.getByDegree());
            out.write(json.toString().getBytes(StandardCharsets.UTF_8));

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String response = in.readLine();
            JSONParser parser = new JSONParser();
            JSONObject responseJson = (JSONObject) parser.parse(response);

            if(Objects.equals(responseJson.get("result"), "success")) {
                return ExecutionResultDTO.builder()
                        .report("Success")
                        .currentState(new RoboStateDTO()) // TODO check with Kevin how RoboState should be returned (information of axis is in the response JSON)
                        .build();
            } else {
                System.out.println("The movement could not be executed");
                throw new RoboOperationFailedException("The movement could not be executed");
            }
        } catch (IOException | ParseException e) {
            System.out.println("No connection to the simulation could be built");
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
