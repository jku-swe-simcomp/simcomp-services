package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.*;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.command_executors.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WebotsCommandExecutionVisitorTest {

    private WebotsCommandExecutionVisitor createVisitor(SessionService service) {
        return new WebotsCommandExecutionVisitor(service,
                new PoseMock(),
                new AdjustJointAngleMock(),
                new GrabMock(),
                new OpenHandMock(),
                new ResetToHomeMock(),
                new SetJointMock());
    }

    @Test
    void visitTest() throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        SessionServiceMock service = new SessionServiceMock(new WebotsSimulationMock("MOCK"));
        WebotsCommandExecutionVisitor commandExecutionVisitor = createVisitor(service);

        service.sessionRenewed = false;
        ExecutionResultDTO result = commandExecutionVisitor.visit(
                new ExecutionCommand.AdjustJointAngleCommand(new JointAngleAdjustmentDTO()),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);

        service.sessionRenewed = false;
        result = commandExecutionVisitor.visit(
                new ExecutionCommand.PoseCommand(new PositionDTO(), new OrientationDTO()),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);

        service.sessionRenewed = false;
        result = commandExecutionVisitor.visit(
                new ExecutionCommand.GrabCommand(),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);

        service.sessionRenewed = false;
        result = commandExecutionVisitor.visit(
                new ExecutionCommand.OpenHandCommand(),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);

        service.sessionRenewed = false;
        result = commandExecutionVisitor.visit(
                new ExecutionCommand.ResetToHomeCommand(),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);

        service.sessionRenewed = false;
        result = commandExecutionVisitor.visit(
                new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()),
                "SessionID");
        assertEquals("Success", result.getReport());
        assertTrue(service.sessionRenewed);
    }

    private static class SessionServiceMock extends SessionService{

        public boolean sessionRenewed = false;
        public SessionServiceMock(WebotsSimulationInstanceService webotsSimulationInstanceService) {
            super(webotsSimulationInstanceService);
        }

        @Override
        public synchronized SimulationInstanceConfig renewSession(String sessionKey) throws SessionNotValidException {
            sessionRenewed = true;
            return null;
        }
    }

    static class WebotsSimulationMock extends WebotsSimulationInstanceService {

        public WebotsSimulationMock(String adaptorName) {
            super(adaptorName);
        }
    }

    static class PoseMock extends PoseCommandExecutor {
         @Override
         public ExecutionResultDTO execute(ExecutionCommand.PoseCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
                return new ExecutionResultDTO("Success");
         }
    }

    static class AdjustJointAngleMock extends AdjustJointAngleCommandExecutor {
        @Override
        public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
            return new ExecutionResultDTO("Success");
        }
    }

    static class GrabMock extends GrabExecutor {
        @Override
        public ExecutionResultDTO execute(ExecutionCommand.GrabCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
            return new ExecutionResultDTO("Success");
        }
    }

    static class OpenHandMock extends OpenHandExecutor {
        @Override
        public ExecutionResultDTO execute(ExecutionCommand.OpenHandCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
            return new ExecutionResultDTO("Success");
        }
    }

    static class ResetToHomeMock extends ResetToHomeExecutor {
        @Override
        public ExecutionResultDTO execute(ExecutionCommand.ResetToHomeCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
            return new ExecutionResultDTO("Success");
        }
    }

    static class SetJointMock extends SetJointExecutor {
        @Override
        public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, SimulationInstanceConfig config) throws RoboOperationFailedException, IOException, ParseException {
            return new ExecutionResultDTO("Success");
        }
    }
}
