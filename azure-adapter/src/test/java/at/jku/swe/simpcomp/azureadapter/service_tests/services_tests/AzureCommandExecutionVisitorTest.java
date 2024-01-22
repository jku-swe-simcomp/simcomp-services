package at.jku.swe.simpcomp.azureadapter.service_tests.services_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.SetJointAngleCommandExecutor;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureCommandExecutionVisitor;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureSessionService;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AzureCommandExecutionVisitorTest {

    @Mock
    private AzureSessionService azureSessionService;

    @Mock
    private SetJointAngleCommandExecutor setJointAngleCommandExecutor;

    @Mock
    private AdjustJointAngleCommandExecutor adjustJointAngleCommandExecutor;

    @InjectMocks
    private AzureCommandExecutionVisitor azureCommandExecutionVisitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVisit_SetJointPositionCommand() throws Exception {
        ExecutionCommand.SetJointPositionCommand command = mock(ExecutionCommand.SetJointPositionCommand.class);
        String sessionKey = "sessionKey";
        SimulationInstanceConfig config = SimulationInstanceConfig.builder().build();
        when(azureSessionService.renewSession(sessionKey)).thenReturn(config);
        when(setJointAngleCommandExecutor.execute(any(), any())).thenReturn(mock(ExecutionResultDTO.class));

        azureCommandExecutionVisitor.visit(command, sessionKey);

        verify(azureSessionService, times(1)).renewSession(sessionKey);
        verify(setJointAngleCommandExecutor, times(1)).execute(command, SimulationInstanceConfig.builder().build());
        verifyNoInteractions(adjustJointAngleCommandExecutor);
    }

    @Test
    void testVisit_AdjustJointAngleCommand() throws Exception {
        ExecutionCommand.AdjustJointAngleCommand command = mock(ExecutionCommand.AdjustJointAngleCommand.class);
        String sessionKey = "sessionKey";
        SimulationInstanceConfig config = SimulationInstanceConfig.builder().build();
        when(azureSessionService.renewSession(sessionKey)).thenReturn(config);
        when(adjustJointAngleCommandExecutor.execute(any(), any())).thenReturn(mock(ExecutionResultDTO.class));

        azureCommandExecutionVisitor.visit(command, sessionKey);

        verify(azureSessionService, times(1)).renewSession(sessionKey);
        verify(adjustJointAngleCommandExecutor, times(1)).execute(command, SimulationInstanceConfig.builder().build());
        verifyNoInteractions(setJointAngleCommandExecutor);
    }

    @Test
    void testVisit_ThrowsException() throws Exception {
        ExecutionCommand.SetJointPositionCommand command = mock(ExecutionCommand.SetJointPositionCommand.class);
        String sessionKey = "sessionKey";

        when(azureSessionService.renewSession(sessionKey)).thenThrow(new Exception("Session renewal failed"));

        assertThrows(Exception.class, () -> azureCommandExecutionVisitor.visit(command, sessionKey));

        verify(azureSessionService, times(1)).renewSession(sessionKey);
        verifyNoInteractions(setJointAngleCommandExecutor, adjustJointAngleCommandExecutor);
    }
}
