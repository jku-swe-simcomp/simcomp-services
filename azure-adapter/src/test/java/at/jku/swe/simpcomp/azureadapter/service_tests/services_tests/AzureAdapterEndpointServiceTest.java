package at.jku.swe.simpcomp.azureadapter.service_tests.services_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureAdaptorEndpointService;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureSessionService;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AzureAdapterEndpointServiceTest {

    @Test
    void testInitSession() throws SessionInitializationFailedException {
        AzureSessionService mockSessionService = mock(AzureSessionService.class);
        when(mockSessionService.initializeSession()).thenReturn("mockedSessionId");

        AzureAdaptorEndpointService endpointService = new AzureAdaptorEndpointService(mockSessionService);
        String sessionId = endpointService.initSession();

        assertEquals("mockedSessionId", sessionId);
    }

    @Test
    void testInitSessionWithInstanceId() throws SessionInitializationFailedException {
        AzureSessionService mockSessionService = mock(AzureSessionService.class);
        when(mockSessionService.initializeSession("testInstanceId")).thenReturn("mockedSessionId");

        AzureAdaptorEndpointService endpointService = new AzureAdaptorEndpointService(mockSessionService);
        String sessionId = endpointService.initSession("testInstanceId");

        assertEquals("mockedSessionId", sessionId);
    }

    @Test
    void testCloseSession() throws SessionNotValidException {
        AzureSessionService mockSessionService = mock(AzureSessionService.class);

        AzureAdaptorEndpointService endpointService = new AzureAdaptorEndpointService(mockSessionService);
        endpointService.closeSession("mockedSessionId");

        verify(mockSessionService).closeSession("mockedSessionId");
    }

    @Test
    void testGetAttributeValueJointPositions() throws SessionNotValidException {
        AzureSessionService mockSessionService = mock(AzureSessionService.class);
        when(mockSessionService.renewSession("mockedSessionId")).thenReturn(null);

        AzureExecutionService mockExecutionService = mock(AzureExecutionService.class);
        when(mockExecutionService.getAllJointAngles("testInstanceId")).thenReturn(List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));

        AzureAdaptorEndpointService endpointService = new AzureAdaptorEndpointService(mockSessionService);
        AttributeValue attributeValue = endpointService.getAttributeValue(AttributeKey.JOINT_POSITIONS, "mockedSessionId");

        assertEquals(List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), attributeValue);
    }

    @Test
    void testGetAttributeValueUnsupportedAttribute() throws SessionNotValidException {
        AzureSessionService mockSessionService = mock(AzureSessionService.class);
        when(mockSessionService.renewSession("mockedSessionId")).thenReturn(null);

        AzureExecutionService mockExecutionService = mock(AzureExecutionService.class);

        AzureAdaptorEndpointService endpointService = new AzureAdaptorEndpointService(mockSessionService);
        assertThrows(UnsupportedOperationException.class, () -> {
            endpointService.getAttributeValue(AttributeKey.POSITION, "mockedSessionId");
        });
    }
}