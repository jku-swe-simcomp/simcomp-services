package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Class to execute an open hand command
 * @see CommandExecutor
 */
@Slf4j
@Service
public class OpenHandExecutor
        implements CommandExecutor<ExecutionCommand.OpenHandCommand, ExecutionResultDTO> {


    /**
     * Method that executes an open hand command at a simulation
     * @param command the command to execute
     * @param config the configuration of the simulation
     * @return the result of the execution
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the created json is invalid
     */
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.OpenHandCommand command, SimulationInstanceConfig config)
            throws RoboOperationFailedException, IOException, ParseException {

        log.info("Executing open hand command");
        JSONObject json = new JSONObject();
        json.put("operation", "set_axis");
        json.put("axis", "axis7");
        json.put("value", 1);
        return WebotsExecutionService.executeCommand(json, config);
    }
}