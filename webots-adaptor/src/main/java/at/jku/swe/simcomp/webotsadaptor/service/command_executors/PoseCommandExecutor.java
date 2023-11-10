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
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class PoseCommandExecutor implements CommandExecutor<ExecutionCommand.PoseCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.PoseCommand command, WebotsSimulationConfig config) throws RoboOperationFailedException, IOException, ParseException {

        JSONObject json = new JSONObject();
        json.put("operation", "get_position");
        return WebotsExecutionService.executeCommand(json, config);
    }
}
