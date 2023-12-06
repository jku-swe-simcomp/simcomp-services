package at.jku.swe.simcomp.manager;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRegistryClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ServiceRegistryClient serviceRegistryClient;


    @Test
    void testGetRegisteredAdaptors() {
        ServiceRegistrationConfigDTO[] mockConfigs = buildConfigs(3);
        lenient().when(restTemplate.getForObject(anyString(), eq(ServiceRegistrationConfigDTO[].class)))
                .thenReturn(mockConfigs);

        // Call the method under test
        List<ServiceRegistrationConfigDTO> result = serviceRegistryClient.getRegisteredAdaptors();

        // Assert the result
        assertEquals(Arrays.asList(mockConfigs), result);
    }
    private ServiceRegistrationConfigDTO[] buildConfigs(int count){
        ServiceRegistrationConfigDTO[] configs = new ServiceRegistrationConfigDTO[count];
        for (int i = 0; i < count; i++) {
            configs[i] = ServiceRegistrationConfigDTO.builder()
                    .name("TestAdaptor" + i)
                    .host("localhost")
                    .port(8080)
                    .supportedActions(Set.of())
                    .build();
        }
        return configs;
    }
}
