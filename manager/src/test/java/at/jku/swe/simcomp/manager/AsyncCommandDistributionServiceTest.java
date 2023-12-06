package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.ExecutionCommandValidationVisitor;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponse;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionResponseRepository;
import at.jku.swe.simcomp.manager.rest.exception.CommandExecutionFailedException;
import at.jku.swe.simcomp.manager.service.AsyncCommandDistributionService;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncCommandDistributionServiceTest {
    @Mock
    private AdaptorClient adaptorClient;
    @Mock
    private ExecutionRepository executionRepository;
    @Mock
    private ExecutionResponseRepository executionResponseRepository;
    @Mock
    private AdaptorSessionRepository adaptorSessionRepository;
    @Mock
    private ExecutionCommandValidationVisitor executionCommandValidationVisitor;
    @InjectMocks
    private AsyncCommandDistributionService asyncCommandDistributionService;

    @Test
    void testDistributeCommand_Success() throws CommandExecutionFailedException {
        // arrange
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("adaptor")
                .state(SessionState.OPEN)
                .build();
        ServiceRegistrationConfigDTO config = ServiceRegistrationConfigDTO.builder()
                .name("adaptor")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of())
                .build();
        ExecutionCommand command = new ExecutionCommand.CompositeCommand(List.of(new ExecutionCommand.GrabCommand()));
        UUID executionId = UUID.randomUUID();


        when(adaptorSessionRepository.findById(anyLong())).thenReturn(Optional.of(adaptorSession));
        when(executionCommandValidationVisitor.visit(any(ExecutionCommand.CompositeCommand.class), any())).thenReturn(true);
        when(adaptorClient.executeCommand(any(), any(), any())).thenReturn(ExecutionResultDTO.builder().report("").build());
        when(executionResponseRepository.save(any())).thenAnswer(invocation -> {
            ExecutionResponse response = invocation.getArgument(0);
            response.setId(1L);
            return response;
        });

        // act
        asyncCommandDistributionService.distributeCommand(1L, executionId, config, command);

        // assert
        verify(executionResponseRepository, times(1)).updateExecutionResponse(anyLong(), eq(200), eq(ExecutionResponseState.SUCCESS), anyString());
    }

    @Test
    void testDistributeCommand_whenAdaptorSessionClosed_thenNoInteraction() throws CommandExecutionFailedException {
        // arrange
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("adaptor")
                .state(SessionState.CLOSED)
                .build();
        ServiceRegistrationConfigDTO config = ServiceRegistrationConfigDTO.builder()
                .name("adaptor")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of())
                .build();
        ExecutionCommand command = new ExecutionCommand.CompositeCommand(List.of(new ExecutionCommand.GrabCommand()));
        UUID executionId = UUID.randomUUID();

        when(adaptorSessionRepository.findById(anyLong())).thenReturn(Optional.of(adaptorSession));

        // act
        asyncCommandDistributionService.distributeCommand(1L, executionId, config, command);

        // assert
        verifyNoInteractions(adaptorClient, executionResponseRepository);
    }

    @Test
    void testDistributeCommand_whenSessionNotFound_thenException() throws CommandExecutionFailedException {
        // arrange
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("adaptor")
                .state(SessionState.CLOSED)
                .build();
        ServiceRegistrationConfigDTO config = ServiceRegistrationConfigDTO.builder()
                .name("adaptor")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of())
                .build();
        ExecutionCommand command = new ExecutionCommand.CompositeCommand(List.of(new ExecutionCommand.GrabCommand()));
        UUID executionId = UUID.randomUUID();

        when(adaptorSessionRepository.findById(anyLong())).thenThrow(new NoSuchElementException());

        // act
        assertThrows(NoSuchElementException.class, () -> asyncCommandDistributionService.distributeCommand(1L, executionId, config, command));

        // assert
        verifyNoInteractions(adaptorClient, executionResponseRepository);
    }

    @Test
    void testDistributeCommand_whenCommandNotSupported_thenCorrespondingResponse() throws CommandExecutionFailedException {
        // arrange
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("adaptor")
                .state(SessionState.OPEN)
                .build();
        ServiceRegistrationConfigDTO config = ServiceRegistrationConfigDTO.builder()
                .name("adaptor")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of())
                .build();
        ExecutionCommand command = new ExecutionCommand.CompositeCommand(List.of(new ExecutionCommand.GrabCommand()));
        UUID executionId = UUID.randomUUID();


        when(adaptorSessionRepository.findById(anyLong())).thenReturn(Optional.of(adaptorSession));
        when(executionCommandValidationVisitor.visit(any(ExecutionCommand.CompositeCommand.class), any())).thenReturn(false);
        when(executionResponseRepository.save(any())).thenAnswer(invocation -> {
            ExecutionResponse response = invocation.getArgument(0);
            response.setId(1L);
            return response;
        });

        // act
        asyncCommandDistributionService.distributeCommand(1L, executionId, config, command);

        // assert
        verify(executionResponseRepository, times(1)).updateExecutionResponse(anyLong(), eq(400), eq(ExecutionResponseState.ERROR), anyString());
        verifyNoInteractions(adaptorClient);
    }

    @Test
    void testDistributeCommand_whenAdaptorSessionNotValid_thenCorrespondingResponse() throws CommandExecutionFailedException {
        // arrange
        AdaptorSession adaptorSession = AdaptorSession.builder()
                .adaptorName("adaptor")
                .id(1L)
                .session(Session.builder().sessionKey(UUID.randomUUID()).build())
                .state(SessionState.OPEN)
                .build();
        ServiceRegistrationConfigDTO config = ServiceRegistrationConfigDTO.builder()
                .name("adaptor")
                .host("localhost")
                .port(8080)
                .supportedActions(Set.of())
                .build();
        ExecutionCommand command = new ExecutionCommand.CompositeCommand(List.of(new ExecutionCommand.GrabCommand()));
        UUID executionId = UUID.randomUUID();


        when(adaptorSessionRepository.findById(anyLong())).thenReturn(Optional.of(adaptorSession));
        when(executionCommandValidationVisitor.visit(any(ExecutionCommand.CompositeCommand.class), any())).thenReturn(true);
        when(adaptorClient.executeCommand(any(), any(), any())).thenThrow(new CommandExecutionFailedException("Unauthorized", 401));
        when(executionResponseRepository.save(any())).thenAnswer(invocation -> {
            ExecutionResponse response = invocation.getArgument(0);
            response.setId(1L);
            return response;
        });

        // act
        asyncCommandDistributionService.distributeCommand(1L, executionId, config, command);

        // assert
        verify(executionResponseRepository, times(1)).updateExecutionResponse(anyLong(), eq(401), eq(ExecutionResponseState.ERROR), anyString());
        verify(adaptorClient, times(1)).executeCommand(any(), any(), any());
        verify(adaptorSessionRepository, times(1)).updateSessionStateById(1L, SessionState.CLOSED);
    }
}
