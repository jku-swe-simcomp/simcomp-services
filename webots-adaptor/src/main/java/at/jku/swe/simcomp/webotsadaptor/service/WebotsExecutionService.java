package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Class to interact with a Webots Simulation. Allows the execution of
 * commands in JSON format, and to query the current position.
 * For the interaction with the simulation the host and the port a required.
 * All methods of the class a static.
 */
@Slf4j
public class WebotsExecutionService {

    /**
     * Method to execute a command at a Webots simulation. Executes the command
     * and returns a success message if the execution was successful. The method
     * throws an exception if the command cannot be executed.
     *
     * @param command The command to be executed in JSON format
     * @param config Contains the location of the simulation (host and port)
     *
     * @return success message if the command was executed, otherwise an exception
     * is thrown
     * @throws RoboOperationFailedException if the simulation cannot execute the
     * command and returns a not successful message
     * @throws IOException if the connection to the simulation is not working
     * @throws ParseException if the input is no valid JSON
     */
    public static ExecutionResultDTO executeCommand(JSONObject command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
        log.info("Connecting to {} on port {}", config.getInstanceHost(), config.getInstancePort());
        log.info("Execute request {}", command);
        try {
            JSONObject responseJson = accessWebots(command, config);

            log.info("Simulation Response: {}", responseJson.toString());

            if(Objects.equals(responseJson.get("result"), "success")) {
                return ExecutionResultDTO.builder()
                        .report("Success")
                        .build();
            } else {
                log.error("The movement could not be executed");
                throw new RoboOperationFailedException("The movement could not be executed");
            }
        } catch (IOException | ParseException e) {
            log.error("No connection to the simulation could be built");
            log.error(e.getMessage());
            throw e;
        }
    }


    /**
     * Method to get the current axis values of a Webots simulation. The method
     * queries the six axis values from the simulation and returns them as a list.
     * The first value in the list is the value of the first axis and so an.
     *
     * @param config Contains the location of the simulation (host and port)
     *
     * @return a List of six double values representing the current values of the
     * six axes.
     * @throws RoboOperationFailedException if the simulation cannot execute the
     * command and returns a not successful message
     * @throws IOException if the connection to the simulation is not working
     * @throws ParseException if the input is no valid JSON
     */
    public static List<Double> getPositions(SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
        log.info("Connecting to {} on port {}, to get the current position", config.getInstanceHost(), config.getInstancePort());
        try {
            JSONObject json = new JSONObject();
            json.put("operation", "get_position");
            JSONObject responseJson = accessWebots(json, config);

            if(Objects.equals(responseJson.get("result"), "success")) {
                log.info("Converting JSON to Double list");
                JSONArray jsonArray = (JSONArray) responseJson.get("positions");
                Iterator<String> iterator = jsonArray.iterator();
                List<Double> positions = new ArrayList<>();
                while(iterator.hasNext()){
                    Object val = iterator.next();
                    if (val instanceof Long) {
                        positions.add(((Long) val).doubleValue());
                    } else if (val instanceof Double) {
                        positions.add((Double) val);
                    }
                }
                return positions;
            } else {
                log.error("The movement could not be executed");
                throw new RoboOperationFailedException("The movement could not be executed");
            }
        } catch (IOException | ParseException e) {
            log.error("No connection to the simulation could be built");
            log.error(e.getMessage());
            throw e;
        }
    }

    protected static JSONObject accessWebots(JSONObject input, SimulationInstanceConfig config) throws IOException, ParseException {
        Socket client = getSocket(config);
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        log.info("Connected to {}", client.getRemoteSocketAddress());

        out.write(input.toString().getBytes(StandardCharsets.UTF_8));

        log.info("Command sent to simulation: {}", input);
        String response = readResponse(client);
        log.info("Response in plain text: {}", response);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(response);
    }

    protected static Socket getSocket(SimulationInstanceConfig config) throws IOException {
        return new Socket(config.getInstanceHost(), config.getInstancePort());
    }

    protected static String readResponse(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        return in.readLine();
    }

}
