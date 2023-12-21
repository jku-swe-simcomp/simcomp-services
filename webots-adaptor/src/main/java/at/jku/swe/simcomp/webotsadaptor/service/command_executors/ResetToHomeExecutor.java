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

@Slf4j
@Service
public class ResetToHomeExecutor implements CommandExecutor<ExecutionCommand.ResetToHomeCommand, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.ResetToHomeCommand command, SimulationInstanceConfig config)
            throws RoboOperationFailedException, IOException, ParseException {

        log.info("Executing set to home position command");
        JSONObject json = new JSONObject();
        json.put("operation", "initial_position");
        return WebotsExecutionService.executeCommand(json, config);
    }
}
