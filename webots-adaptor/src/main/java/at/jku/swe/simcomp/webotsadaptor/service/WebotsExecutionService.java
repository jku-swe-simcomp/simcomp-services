package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class WebotsExecutionService {

    public static ExecutionResultDTO executeCommand(JSONObject command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
        System.out.println("Connecting to " + config.getInstanceHost() + " on port " + config.getInstancePort());
        try {
            JSONObject responseJson = accessWebots(command, config);

            System.out.println("Simulation Response: " + responseJson.toString());

            if(Objects.equals(responseJson.get("result"), "success")) {
                return ExecutionResultDTO.builder()
                        .report("Success")
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

    public static List<Double> getPositions(SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
        System.out.println("Connecting to " + config.getInstanceHost() + " on port "
                + config.getInstancePort() + " to get the current position");
        try {
            JSONObject json = new JSONObject();
            json.put("operation", "get_position");
            JSONObject responseJson = accessWebots(json, config);

            if(Objects.equals(responseJson.get("result"), "success")) {
                JSONArray jsonArray = (JSONArray) responseJson.get("positions");
                Iterator<Double> iterator = jsonArray.iterator();
                List<Double> positions = new ArrayList<>();
                while(iterator.hasNext()){
                    positions.add(iterator.next());
                }
                return positions;
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

    private static JSONObject accessWebots(JSONObject input, SimulationInstanceConfig config) throws IOException, ParseException {
        Socket client = new Socket(config.getInstanceHost(), config.getInstancePort());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        System.out.println("Connected to " + client.getRemoteSocketAddress());

        out.write(input.toString().getBytes(StandardCharsets.UTF_8));

        System.out.println("Command sent to simulation: " + input);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String response = in.readLine();
        System.out.println("Response in plain text: " + response);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response);
    }
}
