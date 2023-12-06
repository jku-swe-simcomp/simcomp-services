package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionStateDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.SessionService;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AdaptorSessionRepository adaptorSessionRepository;

    @Mock
    private ServiceRegistryClient serviceRegistryClient;

    @Mock
    private AdaptorClient adaptorClient;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void testInitSession_SelectedType_Success() throws SessionInitializationFailedException {
        // Arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = ServiceRegistrationConfigDTO.builder()
                        .host("localhost").port(8080).name("test").supportedActions(Set.of()).build();

        SessionRequest.SelectedSimulationTypesSessionRequest selectedSimulationTypesSessionRequest =
                new SessionRequest.SelectedSimulationTypesSessionRequest(List.of("test"));

        when(adaptorClient.getSession(any(ServiceRegistrationConfigDTO.class))).thenReturn(Optional.of("1234"));
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of(serviceRegistrationConfigDTO));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Session session = sessionService.initSession(selectedSimulationTypesSessionRequest);

        // Assert
        assertEquals(1, session.getAdaptorSessions().size());
        assertEquals("test", session.getAdaptorSessions().get(0).getAdaptorName());
        assertEquals("1234", session.getAdaptorSessions().get(0).getSessionKey());
        assertEquals(session, session.getAdaptorSessions().get(0).getSession());
        assertEquals(SessionState.OPEN, session.getAdaptorSessions().get(0).getState());
        verify(adaptorClient, times(1)).getSession(any(ServiceRegistrationConfigDTO.class));
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void testInitSession_SelectedType_NoAdaptorSessionAvailable_Failure() {
        // Arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = ServiceRegistrationConfigDTO.builder()
                .host("localhost").port(8080).name("test").supportedActions(Set.of()).build();
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of(serviceRegistrationConfigDTO));
        when(adaptorClient.getSession(any(ServiceRegistrationConfigDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SessionInitializationFailedException.class, () -> {
            sessionService.initSession(new SessionRequest.SelectedSimulationTypesSessionRequest(List.of("test")));
        });

        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(adaptorClient, times(1)).getSession(serviceRegistrationConfigDTO);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

    @Test
    public void testInitSession_SelectedType_NoRegisteredAdaptors_Failure() {
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of());

        // Act & Assert
        assertThrows(SessionInitializationFailedException.class, () -> {
            sessionService.initSession(new SessionRequest.SelectedSimulationTypesSessionRequest(List.of("test")));
        });

        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(adaptorClient, times(0)).getSession(any(ServiceRegistrationConfigDTO.class));
        verify(sessionRepository, times(0)).save(any(Session.class));
    }
    @Test
    public void testInitSession_SelectedInstance_Success() throws SessionInitializationFailedException {
        // Arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = ServiceRegistrationConfigDTO.builder()
                .host("localhost").port(8080).name("testType").supportedActions(Set.of()).build();
        Map<String, String> requestedInstances = Map.of("testType", "testInstance");

        SessionRequest.SelectedSimulationInstancesSessionRequest selectedSimulationInstancesSessionRequest =
                new SessionRequest.SelectedSimulationInstancesSessionRequest(requestedInstances);

        when(adaptorClient.getSession(serviceRegistrationConfigDTO, "testInstance")).thenReturn(Optional.of("1234"));
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of(serviceRegistrationConfigDTO));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Session session = sessionService.initSession(selectedSimulationInstancesSessionRequest);

        // Assert
        assertEquals(1, session.getAdaptorSessions().size());
        assertEquals("testType", session.getAdaptorSessions().get(0).getAdaptorName());
        assertEquals("testInstance", session.getAdaptorSessions().get(0).getInstanceId());
        assertEquals("1234", session.getAdaptorSessions().get(0).getSessionKey());
        assertEquals(session, session.getAdaptorSessions().get(0).getSession());
        assertEquals(SessionState.OPEN, session.getAdaptorSessions().get(0).getState());
        verify(adaptorClient, times(1)).getSession(serviceRegistrationConfigDTO, "testInstance");
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void testInitSession_SelectedInstance_NoRegisteredAdaptors_Failure() {
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of());

        // Act & Assert
        assertThrows(SessionInitializationFailedException.class, () -> {
            sessionService.initSession(new SessionRequest.SelectedSimulationInstancesSessionRequest(Map.of("testType", "testInstance")));
        });

        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(adaptorClient, times(0)).getSession(any(), any());
        verify(sessionRepository, times(0)).save(any());
    }

    @Test
    public void testInitSession_Any_Success() throws SessionInitializationFailedException {
        // Arrange
        ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = ServiceRegistrationConfigDTO.builder()
                .host("localhost").port(8080).name("testType").supportedActions(Set.of()).build();

        SessionRequest.AnySimulationSessionRequest anySimulationSessionRequest =
                new SessionRequest.AnySimulationSessionRequest(1);

        when(adaptorClient.getSession(serviceRegistrationConfigDTO)).thenReturn(Optional.of("1234"));
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of(serviceRegistrationConfigDTO));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Session session = sessionService.initSession(anySimulationSessionRequest);

        // Assert
        assertEquals(1, session.getAdaptorSessions().size());
        assertEquals("testType", session.getAdaptorSessions().get(0).getAdaptorName());
        assertNull(session.getAdaptorSessions().get(0).getInstanceId());
        assertEquals("1234", session.getAdaptorSessions().get(0).getSessionKey());
        assertEquals(session, session.getAdaptorSessions().get(0).getSession());
        assertEquals(SessionState.OPEN, session.getAdaptorSessions().get(0).getState());
        verify(adaptorClient, times(1)).getSession(serviceRegistrationConfigDTO);
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void closeSession_Success() throws Exception {
        // Arrange
        UUID aggregatedSessionKey = UUID.randomUUID();
        Session session = buildOpenSession(aggregatedSessionKey);
        List<AdaptorSession> adaptorSessions = buildOpenAdaptorSessions(aggregatedSessionKey, 3);
        for (AdaptorSession adaptorSession : adaptorSessions) {
            session.addAdaptorSession(adaptorSession);
        }
        List<ServiceRegistrationConfigDTO> configDTOS = buildServiceRegistrationConfigDTOs(3);

        when(sessionRepository.findBySessionKeyOrElseThrow(aggregatedSessionKey)).thenReturn(session);
        when(adaptorSessionRepository.findBySessionSessionKey(aggregatedSessionKey)).thenReturn(adaptorSessions);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(configDTOS);

        // Act
        sessionService.closeSession(aggregatedSessionKey);

        // Assert
        verify(adaptorClient, times(3)).closeSession(any(), any());
        verify(sessionRepository).updateSessionStateBySessionKey(aggregatedSessionKey, SessionState.CLOSED);
    }

    @Test
    void getSessionState_Success() throws Exception {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        Session session = buildOpenSession(sessionKey);
        List<AdaptorSession> adaptorSessions = buildOpenAdaptorSessions(sessionKey, 3);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);

        // Act
        SessionStateDTO sessionStateDTO = sessionService.getSessionState(sessionKey);

        // Assert
        assertEquals(sessionKey.toString(), sessionStateDTO.sessionKey());
        assertEquals(SessionState.OPEN, sessionStateDTO.sessionState());

        Map<String, SessionState> adaptorStates = sessionStateDTO.simulations();
        assertEquals(3, adaptorStates.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(SessionState.OPEN, adaptorStates.get("Adaptor" + i));
        }

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient);
    }

    @Test
    void addAdaptorSessionToAggregateSession_Success() throws Exception {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "Adaptor0";
        Session session = buildOpenSession(sessionKey);

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(1));
        when(adaptorClient.getSession(any())).thenReturn(Optional.of("adaptorSessionKey"));

        // Act
        sessionService.addAdaptorSessionToAggregatedSession(sessionKey, adaptorName);

        // Assert
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void addAdaptorSessionToAggregateSession_SessionNotAvailable_Failure() throws Exception {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "Adaptor0";
        Session session = buildOpenSession(sessionKey);

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(1));
        when(adaptorClient.getSession(any())).thenReturn(Optional.empty());

        // Act
        assertThrows(SessionInitializationFailedException.class, () -> sessionService.addAdaptorSessionToAggregatedSession(sessionKey, adaptorName));

        // Assert
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

    @Test
    void addAdaptorSessionToAggregateSession_SessionNotFound() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "TestAdaptor";
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenThrow(new NotFoundException("Session not found"));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.addAdaptorSessionToAggregatedSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient, adaptorSessionRepository, serviceRegistryClient);
    }

    @Test
    void addAdaptorSessionToAggregateSession_AdaptorNotRegistered() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "NonExistentAdaptor";
        Session session = buildOpenSession(sessionKey);
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);

        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(1));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.addAdaptorSessionToAggregatedSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient, adaptorSessionRepository);
    }

    @Test
    void closeAdaptorSessionOfAggregateSession_Success() throws Exception {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "Adaptor0";
        Session session = buildOpenSession(sessionKey);
        List<AdaptorSession> adaptorSessions = buildOpenAdaptorSessions(sessionKey, 1);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(1));

        // Act
        sessionService.closeAdaptorSessionOfAggregateSession(sessionKey, adaptorName);

        // Assert
        verify(adaptorClient).closeSession(any(), eq(sessionKey.toString()));
        verify(adaptorSessionRepository).updateSessionStateById(any(Long.class), eq(SessionState.CLOSED));
    }

    @Test
    void closeAdaptorSessionOfAggregateSession_SessionNotFound() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "TestAdaptor";
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenThrow(new NotFoundException("Session not found"));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.closeAdaptorSessionOfAggregateSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient, adaptorSessionRepository, serviceRegistryClient);
    }

    @Test
    void closeAdaptorSessionOfAggregateSession_AdaptorNotPartOfSession() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "NonExistentAdaptor";
        Session session = buildOpenSession(sessionKey);
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.closeAdaptorSessionOfAggregateSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient);
    }

    @Test
    void reopenAdaptorSessionOfAggregateSession_Success() throws Exception {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "Adaptor0";
        Session session = buildOpenSession(sessionKey);
        List<AdaptorSession> adaptorSessions = buildClosedAdaptorSessions(sessionKey, 1);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(1));
        when(adaptorClient.getSession(any())).thenReturn(Optional.of("adaptorSessionKey"));

        // Act
        sessionService.reopenAdaptorSessionOfAggregateSession(sessionKey, adaptorName);

        // Assert
        verify(adaptorClient).getSession(any());
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void reopenAdaptorSessionOfAggregateSession_SessionNotFound() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "TestAdaptor";
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenThrow(new NotFoundException("Session not found"));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.reopenAdaptorSessionOfAggregateSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient, adaptorSessionRepository, serviceRegistryClient);
    }

    @Test
    void reopenAdaptorSessionOfAggregateSession_AdaptorNotPartOfSession() {
        // Arrange
        UUID sessionKey = UUID.randomUUID();
        String adaptorName = "NonExistentAdaptor";
        Session session = buildOpenSession(sessionKey);
        List<AdaptorSession> adaptorSessions = buildClosedAdaptorSessions(sessionKey, 1);
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionKey)).thenReturn(session);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.reopenAdaptorSessionOfAggregateSession(sessionKey, adaptorName));

        // Verify that no interactions occurred with mocked dependencies
        verifyNoInteractions(adaptorClient);
    }

    // private region methods
    private Session buildOpenSession(UUID aggregatedSessionKey) {
        return Session.builder()
                .sessionKey(aggregatedSessionKey)
                .state(SessionState.OPEN)
                .build();
    }

    private Session buildClosedSession(UUID aggregatedSessionKey) {
        return Session.builder()
                .sessionKey(aggregatedSessionKey)
                .state(SessionState.CLOSED)
                .build();
    }

    private List<AdaptorSession> buildClosedAdaptorSessions(UUID sessionKey, int count) {
        List<AdaptorSession> adaptorSessions = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            AdaptorSession adaptorSession = AdaptorSession.builder()
                    .id(i)
                    .sessionKey(sessionKey.toString())
                    .adaptorName("Adaptor" + i)
                    .state(SessionState.CLOSED)
                    .build();
            adaptorSessions.add(adaptorSession);
        }
        return adaptorSessions;
    }

    private List<AdaptorSession> buildOpenAdaptorSessions(UUID aggregatedSessionKey, int count) {
        List<AdaptorSession> adaptorSessions = new ArrayList<>();
        for (long i = 0L; i < count; i++) {
            AdaptorSession adaptorSession = AdaptorSession.builder()
                    .id(i)
                    .sessionKey(aggregatedSessionKey.toString())
                    .adaptorName("Adaptor" + i)
                    .state(SessionState.OPEN)
                    .build();
            adaptorSessions.add(adaptorSession);
        }
        return adaptorSessions;
    }

    private List<ServiceRegistrationConfigDTO> buildServiceRegistrationConfigDTOs(int count) {
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOs = new ArrayList<>();
        for (long i = 0L; i < count; i++) {
            ServiceRegistrationConfigDTO serviceRegistrationConfigDTO = ServiceRegistrationConfigDTO.builder()
                    .name("Adaptor" + i)
                    .host("localhost")
                    .port(8080)
                    .supportedActions(Set.of())
                    .build();
            serviceRegistrationConfigDTOs.add(serviceRegistrationConfigDTO);
        }
        return serviceRegistrationConfigDTOs;
    }
}
