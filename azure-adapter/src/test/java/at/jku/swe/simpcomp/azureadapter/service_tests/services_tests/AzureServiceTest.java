package at.jku.swe.simpcomp.azureadapter.service_tests.services_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureService;
import com.azure.digitaltwins.core.BasicDigitalTwin;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AzureServiceTest {

    @Test
    void testCreateDigitalTwin() {
        DigitalTwinsClient mockClient = mock(DigitalTwinsClient.class);

        AzureService azureService = new AzureService();

        when(AzureExecutionService.buildConnection()).thenReturn(mockClient);

        azureService.createDigitalTwin("testDigitalTwinId");

        verify(mockClient).createOrReplaceDigitalTwin(eq("testDigitalTwinId"), any(BasicDigitalTwin.class), eq(BasicDigitalTwin.class));
    }

    @Test
    void testDeleteDigitalTwin() {
        DigitalTwinsClient mockClient = mock(DigitalTwinsClient.class);

        AzureService azureService = new AzureService();

        when(AzureExecutionService.buildConnection()).thenReturn(mockClient);

        azureService.deleteDigitalTwin("testDigitalTwinId");

        verify(mockClient).deleteDigitalTwin(eq("testDigitalTwinId"));
    }
}
