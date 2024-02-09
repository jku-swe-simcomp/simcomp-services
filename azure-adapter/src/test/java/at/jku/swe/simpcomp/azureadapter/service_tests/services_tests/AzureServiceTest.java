package at.jku.swe.simpcomp.azureadapter.service_tests.services_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.azureadapter.service.Services.AzureService;
import com.azure.digitaltwins.core.BasicDigitalTwin;
import com.azure.digitaltwins.core.DigitalTwinsClient;
import com.azure.digitaltwins.core.DigitalTwinsClientBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.azure.sdk.iot.service.digitaltwin.DigitalTwinClient;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AzureServiceTest {

    @Test
    void testCreateDigitalTwin() {

        when(AzureExecutionService.testBuildConnection()).thenReturn(AzureExecutionService.testBuildConnection());

        AzureService.createDigitalTwin("testDigitalTwinId");

        verify(AzureExecutionService.testBuildConnection()).createOrReplaceDigitalTwin(eq("testDigitalTwinId"), any(BasicDigitalTwin.class), eq(BasicDigitalTwin.class));
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
