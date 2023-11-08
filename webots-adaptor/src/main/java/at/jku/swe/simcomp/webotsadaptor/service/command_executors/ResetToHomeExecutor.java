package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

public class ResetToHomeExecutor implements CommandExecutor<ExecutionCommand.ResetToHomeCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @SneakyThrows
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.ResetToHomeCommand command, WebotsSimulationConfig config) {

        System.out.println("Connecting to " + config.getSimulationEndpointUrl() + " on port " + config.getSimulationPort());

        JSONObject json = new JSONObject();
        json.put("operation", "initial_position");
        return WebotsExecutionService.executeCommand(json, config);
    }
}
