package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class OpenHandExecutor
        implements CommandExecutor<ExecutionCommand.OpenHandCommand, WebotsSimulationConfig, ExecutionResultDTO> {


    @Override
    public ExecutionResultDTO execute(ExecutionCommand.OpenHandCommand command, WebotsSimulationConfig config) throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        json.put("operation", "set_axis");
        json.put("axis", "axis7");
        json.put("value", 1);
        return WebotsExecutionService.executeCommand(json, config);
    }
}