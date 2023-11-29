package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrabExecutor
        implements CommandExecutor<ExecutionCommand.GrabCommand, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.GrabCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getInstanceHost() + " on port " + config.getInstancePort());

        JSONObject json = new JSONObject();
        json.put("operation", "set_axis");
        json.put("axis", "axis7");
        json.put("value", -0.1);
        return WebotsExecutionService.executeCommand(json, config);
    }
}
