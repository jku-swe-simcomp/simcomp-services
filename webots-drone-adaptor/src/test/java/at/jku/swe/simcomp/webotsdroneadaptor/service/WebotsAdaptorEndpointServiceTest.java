package at.jku.swe.simcomp.webotsdroneadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.webotsdroneadaptor.domain.simulation.DroneInstanceRemovalListener;
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
        WebotsDroneAdaptorEndpointService testService = new WebotsDroneAdaptorEndpointService(
                new SessionServiceMock(new WebotsSimulationMock("MOCK")));

        String sessionID = testService.initSession();
        assertEquals(sessionID, "MOCK1");

        sessionID = testService.initSession("SessionID");
        assertEquals(sessionID, "MOCK2");
    }

    @Test
    void testCloseSession() throws SessionNotValidException {
        SessionServiceMock sessionServiceMock = new SessionServiceMock(new WebotsSimulationMock("MOCK"));
        WebotsDroneAdaptorEndpointService testService = new WebotsDroneAdaptorEndpointService(sessionServiceMock);

        testService.closeSession("Test");
        assertFalse(sessionServiceMock.sessionOpen);
    }

    @SneakyThrows
    @Test
    void testGetAttribute() {
        WebotsDroneAdaptorEndpointService testService = new WebotsDroneAdaptorEndpointService(
                new SessionServiceMock(new WebotsSimulationMock("MOCK")));

        List<Double> result = new ArrayList<>();
        result.add(0.0);

        try (MockedStatic<WebotsDroneExecutionService> mockedService = Mockito.mockStatic(WebotsDroneExecutionService.class)) {
            mockedService.when(() -> WebotsDroneExecutionService.getPositions(new SimulationInstanceConfig()))
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

    class SessionServiceMock extends DroneSessionService {

        public boolean sessionOpen = true;

        public SessionServiceMock(WebotsDroneSimulationInstanceService webotsDroneSimulationInstanceService) {
            super(webotsDroneSimulationInstanceService);
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

    class WebotsSimulationMock extends WebotsDroneSimulationInstanceService {

        public WebotsSimulationMock(String adaptorName) {
            super(adaptorName);
        }
    }
}
