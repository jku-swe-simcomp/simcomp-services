package at.jku.swe.simcomp.manager;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.AttributeService;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private ServiceRegistryClient serviceRegistryClient;

    @Mock
    private AdaptorClient adaptorClient;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AdaptorSessionRepository adaptorSessionRepository;

    @InjectMocks
    private AttributeService attributeService;

    @Test
    void getAttributeValues_Success() throws SessionNotValidException {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        Session session = buildSession(sessionId);
        List<AdaptorSession> adaptorSessions = buildAdaptorSessions(sessionId, 2);
        List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs = buildAdaptorConfigs(2);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(registeredAdaptorConfigs);

        when(adaptorClient.getAttributeValue(anyString(), eq(attributeKey), any(ServiceRegistrationConfigDTO.class)))
                    .thenReturn(Optional.of(new AttributeValue.JointPositions(List.of(1.0, 2.0, 3.0, 1.0, 2.0, 3.0))));

        // Act
        Map<String, AttributeValue> attributeValues = attributeService.getAttributeValues(sessionId, attributeKey);

        // Assert
        assertEquals(2, attributeValues.size());
        assertEquals(new AttributeValue.JointPositions(List.of(1.0, 2.0, 3.0, 1.0, 2.0, 3.0)), attributeValues.get("Adaptor0"));
        assertEquals(new AttributeValue.JointPositions(List.of(1.0, 2.0, 3.0, 1.0, 2.0, 3.0)), attributeValues.get("Adaptor1"));

        // Verify that methods were called the expected number of times
        verify(sessionRepository, times(1)).findBySessionKeyOrElseThrow(sessionId);
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(adaptorClient, times(2)).getAttributeValue(eq(sessionId.toString()), eq(attributeKey), any(ServiceRegistrationConfigDTO.class));
    }

    @Test
    void getAttributeValues_when_closedAdaptorSession_then_NoFetching() throws SessionNotValidException {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        Session session = buildSession(sessionId);
        List<AdaptorSession> adaptorSessions = buildAdaptorSessions(sessionId, 2);
        adaptorSessions.forEach(adaptorSession -> adaptorSession.setState(SessionState.CLOSED));
        List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs = buildAdaptorConfigs(2);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(registeredAdaptorConfigs);

        // Act
        Map<String, AttributeValue> attributeValues = attributeService.getAttributeValues(sessionId, attributeKey);

        // Assert
        assertEquals(0, attributeValues.size());

        // Verify that methods were called the expected number of times
        verify(sessionRepository, times(1)).findBySessionKeyOrElseThrow(sessionId);
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verifyNoInteractions(adaptorClient);
    }

    @Test
    void getAttributeValues_when_sessionNotValidException_then_UpdateSessionState() throws SessionNotValidException {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        AttributeKey attributeKey = AttributeKey.JOINT_POSITIONS;
        Session session = buildSession(sessionId);
        List<AdaptorSession> adaptorSessions = buildAdaptorSessions(sessionId, 2);
        List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs = buildAdaptorConfigs(2);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }

        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(registeredAdaptorConfigs);
        when(adaptorClient.getAttributeValue(anyString(), eq(attributeKey), any(ServiceRegistrationConfigDTO.class)))
                .thenThrow(new SessionNotValidException("Session not valid"));
        // Act
        Map<String, AttributeValue> attributeValues = attributeService.getAttributeValues(sessionId, attributeKey);

        // Assert
        assertEquals(2, attributeValues.size());
        assertEquals(null, attributeValues.get("Adaptor0"));
        assertEquals(null, attributeValues.get("Adaptor1"));

        // Verify that methods were called the expected number of times
        verify(sessionRepository, times(1)).findBySessionKeyOrElseThrow(sessionId);
        verify(serviceRegistryClient, times(1)).getRegisteredAdaptors();
        verify(adaptorClient, times(2)).getAttributeValue(eq(sessionId.toString()), eq(attributeKey), any(ServiceRegistrationConfigDTO.class));
        verify(adaptorSessionRepository, times(2)).updateSessionStateById(anyLong(), eq(SessionState.CLOSED));
    }

    // Utility methods for creating test data

    private Session buildSession(UUID sessionKey) {
        return Session.builder()
                .sessionKey(sessionKey)
                .state(SessionState.OPEN)
                .build();
    }

    private List<AdaptorSession> buildAdaptorSessions(UUID sessionKey, int count) {
        List<AdaptorSession> adaptorSessions = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            AdaptorSession adaptorSession = AdaptorSession.builder()
                    .id(i)
                    .sessionKey(sessionKey.toString())
                    .adaptorName("Adaptor" + i)
                    .state(SessionState.OPEN)
                    .build();
            adaptorSessions.add(adaptorSession);
        }
        return adaptorSessions;
    }

    private List<ServiceRegistrationConfigDTO> buildAdaptorConfigs(int count) {
        List<ServiceRegistrationConfigDTO> adaptorConfigs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ServiceRegistrationConfigDTO config = new ServiceRegistrationConfigDTO();
            config.setName("Adaptor" + i);
            // Set other properties as needed
            adaptorConfigs.add(config);
        }
        return adaptorConfigs;
    }
}

