package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.service.SimulationInstanceService;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationInstanceServiceTest {
    @Mock
    private ServiceRegistryClient serviceRegistryClient;

    @Mock
    private AdaptorClient adaptorClient;

    @InjectMocks
    private SimulationInstanceService simulationInstanceService;

    @Test
    void testGetAvailableSimulations() {
        // arrange
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(2);
        var configsForView = serviceRegistrationConfigDTOS.stream()
                .map(ServiceRegistrationConfigDTO::viewForDisplay)
                .toList();
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        // act
        var response = simulationInstanceService.getAvailableSimulations();
        // assert
        assertEquals(configsForView, response);
    }

    @Test
    void testRegisterSimulationInstanceForAdaptor() throws Exception {
        // arrange
        var config = new SimulationInstanceConfig("Adaptor0","instance", "localhost", 8080);
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(1);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        // act
        simulationInstanceService.registerSimulationInstanceForAdaptor(config);
        // assert
        verify(adaptorClient).registerSimulationInstanceForAdaptor(serviceRegistrationConfigDTOS.get(0), config);
    }

    @Test
    void testRegisterSimulationInstanceForAdaptor_whenSimulationNotRegistered_thenException() throws Exception {
        // arrange
        var config = new SimulationInstanceConfig("Adaptor0","instance", "localhost", 8080);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of());
        // act
        assertThrows(NotFoundException.class, () -> simulationInstanceService.registerSimulationInstanceForAdaptor(config));
    }
    @Test
    void testGetSimulationInstances() {
        // arrange
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(2);
        var config = new SimulationInstanceConfig("Adaptor0","instance", "localhost", 8080);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        when(adaptorClient.getSimulationInstances(serviceRegistrationConfigDTOS.get(0))).thenReturn(List.of(config));
        // act
        var response = simulationInstanceService.getSimulationInstances();
        // assert
        assertEquals(1, response.size());
        assertEquals(response.get(0).getSimulationType(), config.getSimulationType());
        assertEquals(response.get(0).getInstanceId(), config.getInstanceId());
        assertEquals(response.get(0).getInstancePort(), config.getInstancePort());
        assertEquals(response.get(0).getInstanceHost(), config.getInstanceHost());
    }

    @Test
    void testGetSimulationInstancesForSpecificAdaptor() {
        // arrange
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(2);
        var config = new SimulationInstanceConfig("Adaptor0","instance", "localhost", 8080);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        when(adaptorClient.getSimulationInstances(serviceRegistrationConfigDTOS.get(0))).thenReturn(List.of(config));
        // act
        var response = simulationInstanceService.getSimulationInstances("Adaptor0");
        // assert
        assertEquals(1, response.size());
        assertEquals(response.get(0).getSimulationType(), config.getSimulationType());
        assertEquals(response.get(0).getInstanceId(), config.getInstanceId());
        assertEquals(response.get(0).getInstancePort(), config.getInstancePort());
        assertEquals(response.get(0).getInstanceHost(), config.getInstanceHost());
    }

    @Test
    void testGetSimulationInstancesForSpecificAdaptor_whenSimulationNotRegistered_thenException() {
        // arrange
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(2);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        // act
        assertThrows(NotFoundException.class, () -> simulationInstanceService.getSimulationInstances("NON_EXISTENT_ADAPTOR"));
        verifyNoInteractions(adaptorClient);
    }

    @Test
    void testDeleteSimulationInstance(){
        // arrange
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigDTOS = buildAdaptorConfigs(2);
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(serviceRegistrationConfigDTOS);
        // act
        simulationInstanceService.deleteSimulationInstance("Adaptor0", "instance");
        // assert
        verify(adaptorClient).deleteSimulationInstance(serviceRegistrationConfigDTOS.get(0), "instance");
    }

    @Test
    void testDeleteSimulationInstance_whenSimulationNotRegistered_thenException(){
        // arrange
        when(serviceRegistryClient.getRegisteredAdaptors()).thenReturn(List.of());
        // act
        assertThrows(NotFoundException.class, () -> simulationInstanceService.deleteSimulationInstance("Adaptor0", "instance"));
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
