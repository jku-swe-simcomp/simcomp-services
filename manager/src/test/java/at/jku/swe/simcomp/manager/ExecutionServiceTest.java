package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionDTO;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Execution;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponse;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.AsyncCommandDistributionService;
import at.jku.swe.simcomp.manager.service.ExecutionService;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecutionServiceTest {
    @Mock
    private AdaptorSessionRepository adaptorSessionRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private ExecutionRepository executionRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AsyncCommandDistributionService commandDistributionService;
    @Mock
    private ServiceRegistryClient serviceRegistryClient;

    @InjectMocks
    private ExecutionService executionService;


    @Test
    void testExecuteCommand_Success() throws BadRequestException {
        // arrange
        UUID sessionId = UUID.randomUUID();
        ExecutionCommand executionCommand = buildExecutionCommand();
        Session session = buildOpenSession(sessionId);
        List<AdaptorSession> adaptorSessions = buildOpenAdaptorSessions(sessionId, 3);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(3));
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(adaptorSessionRepository.findBySessionSessionKey(sessionId)).thenReturn(adaptorSessions);

        // act
        executionService.executeCommand(sessionId, executionCommand);

        // assert
        verify(commandDistributionService, times(1)).distributeCommand(eq(0L), any(), any(), eq(executionCommand));
        verify(commandDistributionService, times(1)).distributeCommand(eq(1L), any(), any(), eq(executionCommand));
        verify(commandDistributionService, times(1)).distributeCommand(eq(2L), any(), any(), eq(executionCommand));
    }

    @Test
    void testExecuteCommand_AdaptorSessionsClosed() throws BadRequestException {
        // arrange
        UUID sessionId = UUID.randomUUID();
        ExecutionCommand executionCommand = buildExecutionCommand();
        Session session = buildOpenSession(sessionId);
        List<AdaptorSession> adaptorSessions = buildClosedAdaptorSessions(sessionId, 3);
        for(AdaptorSession adaptorSession : adaptorSessions){
            session.addAdaptorSession(adaptorSession);
        }
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(buildServiceRegistrationConfigDTOs(3));
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);
        when(adaptorSessionRepository.findBySessionSessionKey(sessionId)).thenReturn(adaptorSessions);

        // act
        executionService.executeCommand(sessionId, executionCommand);

        // assert
        verifyNoInteractions(commandDistributionService);
    }

    @Test
    void testExecuteCommand_SessionClosed() throws BadRequestException {
        // arrange
        UUID sessionId = UUID.randomUUID();
        ExecutionCommand executionCommand = buildExecutionCommand();
        Session session = buildClosedSession(sessionId);
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenReturn(session);

        // act & assert
        assertThrows(BadRequestException.class,() -> executionService.executeCommand(sessionId, executionCommand));
        verifyNoInteractions(commandDistributionService, serviceRegistryClient, adaptorSessionRepository);
    }
    @Test
    void testExecuteCommand_SessionNotFound() {
        // arrange
        UUID sessionId = UUID.randomUUID();
        ExecutionCommand executionCommand = buildExecutionCommand();
        when(sessionRepository.findBySessionKeyOrElseThrow(sessionId)).thenThrow(new NotFoundException("Session not found"));

        // act & assert
        assertThrows(NotFoundException.class, () -> executionService.executeCommand(sessionId, executionCommand));
    }

    @Test
    void testGetExecution(){
        // arrange
        UUID sessionId = UUID.randomUUID();
        Session session = buildOpenSession(sessionId);
        UUID executionId = UUID.randomUUID();
        Execution execution = Execution.builder()
                .executionId(executionId)
                .command("command-json")
                .build();
        session.addExecution(execution);
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("Adaptor")
                .build();
        ExecutionResponse executionResponse = ExecutionResponse.builder()
                .responseCode(200L)
                .state(ExecutionResponseState.SUCCESS)
                .report("report")
                .adaptorSession(adaptorSession)
                .build();
        execution.addExecutionResponse(executionResponse);
        when(executionRepository.findByExecutionUUIDOrElseThrow(executionId)).thenReturn(execution);

        // act
        ExecutionDTO executionDTO = executionService.getExecution(executionId);

        // assert
        verify(executionRepository, times(1)).findByExecutionUUIDOrElseThrow(executionId);
        assertEquals(executionId.toString(), executionDTO.id());
        assertEquals("command-json", executionDTO.command());
        assertEquals(1, executionDTO.responses().size());
        assertEquals(200, executionDTO.responses().get(0).responseCode());
        assertEquals(ExecutionResponseState.SUCCESS, executionDTO.responses().get(0).state());
        assertEquals("report", executionDTO.responses().get(0).report());
        assertEquals("Adaptor", executionDTO.responses().get(0).simulationName());
        assertEquals(sessionId.toString(), executionDTO.sessionId());
    }
    // private region
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
    private ExecutionCommand buildExecutionCommand() {
        return new ExecutionCommand.AdjustJointAngleCommand(new JointAngleAdjustmentDTO(RoboJoint.AXIS_1, 1.2));
    }

}
