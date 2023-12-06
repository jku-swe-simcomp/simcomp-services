package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    SimulationInstanceConfig SIMULATION_INSTANCE_CONFIG_MOCK_1 = new SimulationInstanceConfig(
            "MOCK",
            "SOME_ID",
            "host",
            1234
    );
    SimulationInstanceConfig SIMULATION_INSTANCE_CONFIG_MOCK_2 = new SimulationInstanceConfig(
            "MOCK",
            "ID",
            "host",
            1234
    );

    private final WebotsSimulationMock simulationMock = new WebotsSimulationMock("MockSimulation");

    SessionService createSessionService() {
        return new SessionService(simulationMock);
    }

    @Test
    void initializeSessionTest() throws Exception {

        try (MockedStatic<WebotsSimulationInstanceService> mockedService = Mockito.mockStatic(WebotsSimulationInstanceService.class)) {
            SessionService sessionService = createSessionService();

            Set<SimulationInstanceConfig> instances = new HashSet<>();
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            assertThrows(SessionInitializationFailedException.class, sessionService::initializeSession);
            assertThrows(SessionInitializationFailedException.class, () -> sessionService.initializeSession("InvalidID"));
            assertThrows(SessionInitializationFailedException.class, () -> sessionService.initializeSession(Optional.empty()));

            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_2);
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
                UUID uuidOne = new UUID(10, 100);
                mockedUUID.when(UUID::randomUUID).thenReturn(uuidOne);

                String sessionKeyOne = sessionService.initializeSession("ID");
                assertEquals(sessionKeyOne, "00000000-0000-000a-0000-000000000064");

                UUID uuidTwo = new UUID(10, 200);
                mockedUUID.when(UUID::randomUUID).thenReturn(uuidTwo);

                String sessionKeyTwo = sessionService.initializeSession();
                assertEquals(sessionKeyTwo, "00000000-0000-000a-0000-0000000000c8");

                assertThrows(SessionInitializationFailedException.class, sessionService::initializeSession);
                assertThrows(SessionInitializationFailedException.class, () -> sessionService.initializeSession("ID"));

                sessionService.closeSession(sessionKeyOne);
                sessionService.closeSession(sessionKeyTwo);
            }
        }
    }

    @Test
    void renewSessionTest() throws SessionInitializationFailedException, SessionNotValidException {
        try (MockedStatic<WebotsSimulationInstanceService> mockedService = Mockito.mockStatic(WebotsSimulationInstanceService.class)) {
            SessionService sessionService = createSessionService();

            Set<SimulationInstanceConfig> instances = new HashSet<>();
            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            String sessionKey = sessionService.initializeSession();
            SimulationInstanceConfig renewedSession = sessionService.renewSession(sessionKey);
            assertEquals("SOME_ID", renewedSession.getInstanceId());
            assertThrows(SessionNotValidException.class, () -> sessionService.renewSession("INVALID_KEY"));

            sessionService.closeSession(sessionKey);
        }
    }

    @Test
    void closeSessionTest() throws SessionInitializationFailedException, SessionNotValidException {
        try (MockedStatic<WebotsSimulationInstanceService> mockedService = Mockito.mockStatic(WebotsSimulationInstanceService.class)) {
            SessionService sessionService = createSessionService();

            Set<SimulationInstanceConfig> instances = new HashSet<>();
            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            String sessionKey = sessionService.initializeSession();
            sessionService.closeSession(sessionKey);
            assertThrows(SessionNotValidException.class, () -> sessionService.closeSession(sessionKey));
        }
    }

    @Test
    void getConfigForSessionTest() throws SessionInitializationFailedException, SessionNotValidException {
        try (MockedStatic<WebotsSimulationInstanceService> mockedService = Mockito.mockStatic(WebotsSimulationInstanceService.class)) {
            SessionService sessionService = createSessionService();

            Set<SimulationInstanceConfig> instances = new HashSet<>();
            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            String sessionKey = sessionService.initializeSession();
            SimulationInstanceConfig config = sessionService.getConfigForSession(sessionKey);
            assertEquals("SOME_ID", config.getInstanceId());
            assertThrows(SessionNotValidException.class, () -> sessionService.closeSession("INVALID_KEY"));
            sessionService.closeSession(sessionKey);
        }
    }

    @Test
    void onSimulationRemovedTest() throws SessionInitializationFailedException, SessionNotValidException {
        try (MockedStatic<WebotsSimulationInstanceService> mockedService = Mockito.mockStatic(WebotsSimulationInstanceService.class)) {
            SessionService sessionService = createSessionService();

            Set<SimulationInstanceConfig> instances = new HashSet<>();
            instances.add(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            mockedService.when(WebotsSimulationInstanceService::getInstances)
                    .thenReturn(instances);

            String sessionKey = sessionService.initializeSession();
            SimulationInstanceConfig config = sessionService.getConfigForSession(sessionKey);
            assertEquals("SOME_ID", config.getInstanceId());
            sessionService.onSimulationRemoved(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            sessionService.onSimulationRemoved(SIMULATION_INSTANCE_CONFIG_MOCK_1);
            assertThrows(SessionNotValidException.class, () -> sessionService.closeSession(sessionKey));
        }
    }

    class WebotsSimulationMock extends WebotsSimulationInstanceService {

        public WebotsSimulationMock(String adaptorName) {
            super(adaptorName);
        }
    }
}
