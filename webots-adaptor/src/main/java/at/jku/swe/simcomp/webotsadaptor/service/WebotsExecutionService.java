package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WebotsExecutionService {

    public static ExecutionResultDTO executeCommand(JSONObject command, WebotsSimulationConfig config)
            throws RoboOperationFailedException, IOException, ParseException {
        System.out.println("Connecting to " + config.getSimulationEndpointUrl()
                + " on port " + config.getSimulationPort());
        Socket client;
        DataOutputStream out;
        try {
            client = new Socket(config.getSimulationEndpointUrl(), config.getSimulationPort());
            out = new DataOutputStream(client.getOutputStream());
            System.out.println("Connected to " + client.getRemoteSocketAddress());

            out.write(command.toString().getBytes(StandardCharsets.UTF_8));

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
        } catch (IOException | ParseException | RoboOperationFailedException e) {
            System.out.println("No connection to the simulation could be built");
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
