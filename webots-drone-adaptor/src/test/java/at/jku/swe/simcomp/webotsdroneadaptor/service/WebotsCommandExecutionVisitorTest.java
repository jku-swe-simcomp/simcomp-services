package at.jku.swe.simcomp.webotsdroneadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.*;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsdroneadaptor.service.command_executors.CustomExecutor;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WebotsCommandExecutionVisitorTest {

    private WebotsDroneCommandExecutionVisitor createVisitor(DroneSessionService service) {
        return new WebotsDroneCommandExecutionVisitor(service,
                new CustomMock());
    }

    @Test
    void visitTest() throws Exception {
        SessionServiceMock service = new SessionServiceMock(new WebotsSimulationMock("MOCK"));
        WebotsDroneCommandExecutionVisitor commandExecutionVisitor = createVisitor(service);

        service.sessionRenewed = false;
        ExecutionResultDTO result = commandExecutionVisitor.visit(
                new ExecutionCommand.CustomCommand(
                        "{\"operation\":\"set_altitude\", \"value\":5.0}"
                ),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);
    }

    private static class SessionServiceMock extends DroneSessionService {

        public boolean sessionRenewed = false;
        public SessionServiceMock(WebotsDroneSimulationInstanceService webotsSimulationInstanceService) {
            super(webotsSimulationInstanceService);
        }

        @Override
        public synchronized SimulationInstanceConfig renewSession(String sessionKey) throws SessionNotValidException {
            sessionRenewed = true;
            return null;
        }
    }

    static class WebotsSimulationMock extends WebotsDroneSimulationInstanceService {

        public WebotsSimulationMock(String adaptorName) {
            super(adaptorName);
        }
    }

    static class CustomMock extends CustomExecutor {
         @Override
         public ExecutionResultDTO execute(ExecutionCommand.CustomCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
                return new ExecutionResultDTO("Success");
         }
    }
}
