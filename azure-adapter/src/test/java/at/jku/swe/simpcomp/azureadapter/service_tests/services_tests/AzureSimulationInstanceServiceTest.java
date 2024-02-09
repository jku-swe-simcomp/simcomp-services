package at.jku.swe.simpcomp.azureadapter.service_tests.services_tests;

import at.jku.swe.simcomp.azureadapter.service.Services.AzureSimulationInstanceService;
import at.jku.swe.simcomp.azureadapter.simulation.AzureSimulationRemovalListener;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AzureSimulationInstanceServiceTest {

    @Test
    void testAddSimulationInstance() throws BadRequestException {
        String adaptorName = "testAdaptor";
        AzureSimulationInstanceService service = new AzureSimulationInstanceService(adaptorName);

        SimulationInstanceConfig config = new SimulationInstanceConfig(adaptorName, "testInstanceId", "localhost", 1234);

        assertDoesNotThrow(() -> service.addSimulationInstance(config));
        assertTrue(AzureSimulationInstanceService.getInstances().contains(config));
    }

    @Test
    void testAddSimulationInstanceWithInvalidName() {
        String adaptorName = "testAdaptor";
        AzureSimulationInstanceService service = new AzureSimulationInstanceService(adaptorName);

        SimulationInstanceConfig config = new SimulationInstanceConfig("invalidAdaptor", "testInstanceId", "localhost", 1234);

        assertThrows(BadRequestException.class, () -> service.addSimulationInstance(config));
        assertTrue(AzureSimulationInstanceService.getInstances().isEmpty());
    }

    @Test
    void testAddSimulationInstanceWithExistingInstance() throws BadRequestException {
        String adaptorName = "testAdaptor";
        AzureSimulationInstanceService service = new AzureSimulationInstanceService(adaptorName);

        SimulationInstanceConfig config = new SimulationInstanceConfig(adaptorName, "testInstanceId", "localhost", 8080);

        assertDoesNotThrow(() -> service.addSimulationInstance(config));
        assertThrows(BadRequestException.class, () -> service.addSimulationInstance(config));
        assertTrue(AzureSimulationInstanceService.getInstances().contains(config));
        assertEquals(1, AzureSimulationInstanceService.getInstances().size());
    }

    @Test
    void testRemoveSimulationInstance() {
        AzureSimulationRemovalListener mockListener = mock(AzureSimulationRemovalListener.class);

        AzureSimulationInstanceService service = new AzureSimulationInstanceService("testAdaptor");
        service.addSimulationRemovalListener(mockListener);

        SimulationInstanceConfig config = new SimulationInstanceConfig("testAdaptor", "testInstanceId", "localhost", 8080);

        assertDoesNotThrow(() -> service.addSimulationInstance(config));

        assertDoesNotThrow(() -> service.removeSimulationInstance(config.getInstanceId()));
        assertFalse(AzureSimulationInstanceService.getInstances().contains(config));
        verify(mockListener, times(1)).onSimulationRemoved(config);
    }

    @Test
    void testGetSimulationInstances() throws Exception {
        AzureSimulationInstanceService service = new AzureSimulationInstanceService("testAdaptor");

        SimulationInstanceConfig config = new SimulationInstanceConfig("testAdaptor", "testInstanceId", "localhost", 8080);
        service.addSimulationInstance(config);

        assertEquals(1, service.getSimulationInstances().size());
    }

    @Test
    void testCleanUpInstances() {
        AzureSimulationInstanceService service = new AzureSimulationInstanceService("testAdaptor");

        SimulationInstanceConfig config = new SimulationInstanceConfig("testAdaptor", "testInstanceId", "localhost", 8080);
        assertDoesNotThrow(() -> service.addSimulationInstance(config));

        assertDoesNotThrow(() -> service.cleanUpInstances());
        assertTrue(AzureSimulationInstanceService.getInstances().isEmpty());
    }
}