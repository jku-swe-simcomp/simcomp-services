package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WebotsExecutionServiceTest {

    SimulationInstanceConfig SIMULATION_INSTANCE_CONFIG_MOCK = new SimulationInstanceConfig(
            "MOCK",
            "SOME_ID",
            "host",
            1234
    );

    @Test
    void executeCommandTest() throws RoboOperationFailedException, IOException, ParseException {

        try (MockedStatic<WebotsExecutionService> mockedService = Mockito.mockStatic(WebotsExecutionService.class)) {

            JSONObject commandMock = new JSONObject();
            commandMock.put("operation", "adjust_axis");
            commandMock.put("axis", 1);
            commandMock.put("value", 0.0);

            Socket socket = new SocketMock();

            mockedService.when(() -> WebotsExecutionService.getSocket(SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenReturn(socket);
            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn("{\"result\": \"success\"}\n");
            mockedService.when(() -> WebotsExecutionService.executeCommand(commandMock, SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenCallRealMethod();
            mockedService.when(() -> WebotsExecutionService.accessWebots(commandMock, SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenCallRealMethod();

            ExecutionResultDTO result = WebotsExecutionService.executeCommand(commandMock, SIMULATION_INSTANCE_CONFIG_MOCK);
            assertEquals("Success", result.getReport());

            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn("{\"result\": \"error\"}\n");
            assertThrows(RoboOperationFailedException.class, () -> WebotsExecutionService.executeCommand(commandMock, SIMULATION_INSTANCE_CONFIG_MOCK));

            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn("\"result\": \"error\"}\n");
            assertThrows(ParseException.class, () -> WebotsExecutionService.executeCommand(commandMock, SIMULATION_INSTANCE_CONFIG_MOCK));

        }
    }

    @Test
    void getPositionTest() throws RoboOperationFailedException, IOException, ParseException, JSONException {
        try (MockedStatic<WebotsExecutionService> mockedService = Mockito.mockStatic(WebotsExecutionService.class)) {

            Socket socket = new SocketMock();
            JSONObject json = new JSONObject();
            json.put("operation", "get_position");

            JSONObject resultJSONMock = new JSONObject();
            resultJSONMock.put("result", "success");
            JSONArray array = new JSONArray(new double[]{1.0, 1.1, 1.0, 1.0, 1.0, 1.0});
            resultJSONMock.put("positions", array);

            mockedService.when(() -> WebotsExecutionService.getSocket(SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenReturn(socket);
            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn(resultJSONMock.toJSONString());
            mockedService.when(() -> WebotsExecutionService.getPositions(SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenCallRealMethod();
            mockedService.when(() -> WebotsExecutionService.accessWebots(json, SIMULATION_INSTANCE_CONFIG_MOCK))
                    .thenCallRealMethod();

            List<Double> result = WebotsExecutionService.getPositions(SIMULATION_INSTANCE_CONFIG_MOCK);
            assertEquals(1.0, result.get(0));

            resultJSONMock.put("result", "error");
            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn(resultJSONMock.toString());
            assertThrows(RoboOperationFailedException.class, () -> WebotsExecutionService.getPositions(SIMULATION_INSTANCE_CONFIG_MOCK));

            mockedService.when(() -> WebotsExecutionService.readResponse(socket))
                    .thenReturn("\"result\": \"error\"}\n");
            assertThrows(ParseException.class, () -> WebotsExecutionService.getPositions(SIMULATION_INSTANCE_CONFIG_MOCK));

        }
    }

    static class SocketMock extends Socket {

        @Override
        public OutputStream getOutputStream(){
            return System.out;
        }
    }
}
