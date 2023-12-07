package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.OrientationDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResetToHomeExecutorTest {

    SimulationInstanceConfig SIMULATION_INSTANCE_CONFIG_MOCK = new SimulationInstanceConfig(
            "MOCK",
            "SOME_ID",
            "host",
            1234
    );

    @Test
    void executeCommandTest() throws RoboOperationFailedException, IOException, ParseException {
        try (MockedStatic<WebotsExecutionService> mockedService = Mockito.mockStatic(WebotsExecutionService.class)) {

            ExecutionResultDTO result = new ExecutionResultDTO("Success");
            JSONObject json = new JSONObject();
            json.put("operation", "initial_position");

            mockedService.when(() -> WebotsExecutionService.executeCommand(json, SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenReturn(result);

            ExecutionCommand.ResetToHomeCommand command = new ExecutionCommand.ResetToHomeCommand();

            ResetToHomeExecutor executor = new ResetToHomeExecutor();
            ExecutionResultDTO test_result = executor.execute(command, SIMULATION_INSTANCE_CONFIG_MOCK);
            assertEquals(result.hashCode(), test_result.hashCode());
        }
    }
}
