package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WebotsAdaptorEndpointServiceTest {

    @Test
    void testInitSession() throws SessionInitializationFailedException {
        WebotsAdaptorEndpointService testService = new WebotsAdaptorEndpointService(
                new SessionServiceMock(new WebotsSimulationMock("MOCK")));

        String sessionID = testService.initSession();
        assertEquals(sessionID, "MOCK1");

        sessionID = testService.initSession("SessionID");
        assertEquals(sessionID, "MOCK2");
    }

    @Test
    void testCloseSession() throws SessionNotValidException {
        SessionServiceMock sessionServiceMock = new SessionServiceMock(new WebotsSimulationMock("MOCK"));
        WebotsAdaptorEndpointService testService = new WebotsAdaptorEndpointService(sessionServiceMock);

        testService.closeSession("Test");
        assertFalse(sessionServiceMock.sessionOpen);
    }

    @SneakyThrows
    @Test
    void testGetAttribute() {
        WebotsAdaptorEndpointService testService = new WebotsAdaptorEndpointService(
                new SessionServiceMock(new WebotsSimulationMock("MOCK")));

        List<Double> result = new ArrayList<>();
        result.add(0.0);

        try (MockedStatic<WebotsExecutionService> mockedService = Mockito.mockStatic(WebotsExecutionService.class)) {
            mockedService.when(() -> WebotsExecutionService.getPositions(new SimulationInstanceConfig()))
                    .thenReturn(result);

            AttributeValue attributeValue = testService.getAttributeValue(AttributeKey.JOINT_POSITIONS, "Mock");
            AttributeValue.JointPositions jointPositions = (AttributeValue.JointPositions) attributeValue;
            assertEquals(jointPositions.jointPositions().get(0), 0.0);
        }

        assertThrows(UnsupportedOperationException.class,
                () -> testService.getAttributeValue(AttributeKey.POSE, "Mock"));
        assertThrows(UnsupportedOperationException.class,
                () -> testService.getAttributeValue(AttributeKey.JOINT_STATES, "Mock"));
        assertThrows(UnsupportedOperationException.class,
                () -> testService.getAttributeValue(AttributeKey.ORIENTATION, "Mock"));
        assertThrows(UnsupportedOperationException.class,
                () -> testService.getAttributeValue(AttributeKey.POSITION, "Mock"));
    }

    class SessionServiceMock extends SessionService {

        public boolean sessionOpen = true;

        public SessionServiceMock(WebotsSimulationInstanceService webotsSimulationInstanceService) {
            super(webotsSimulationInstanceService);
        }

        @Override
        public synchronized String initializeSession() {
            return "MOCK1";
        }

        @Override
        public synchronized String initializeSession(String instanceId) {
            return "MOCK2";
        }

        @Override
        public synchronized void closeSession (String sessionKey) throws SessionNotValidException {
            if(sessionKey.equals("Test")){
                this.sessionOpen = false;
            }
        }

        @Override
        public SimulationInstanceConfig getConfigForSession(String sessionKey) throws SessionNotValidException {
            return new SimulationInstanceConfig();
        }
    }

    class WebotsSimulationMock extends WebotsSimulationInstanceService {

        public WebotsSimulationMock(String adaptorName) {
            super(adaptorName);
        }
    }
}
