package at.jku.swe.simcomp.serviceregistry;

import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.service.AdaptorClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdaptorClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AdaptorClient adaptorClient;

    @Test
    public void testIsHealthy_True() {
        // Arrange
        Adaptor adaptor = new Adaptor();
        adaptor.setHost("localhost");
        adaptor.setPort(8080);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(null);

        // Act
        boolean result = adaptorClient.isHealthy(adaptor);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsHealthy_False() {
        // Arrange
        Adaptor adaptor = new Adaptor();
        adaptor.setHost("localhost");
        adaptor.setPort(8080);

        doThrow(new RuntimeException("Simulating health check failure")).when(restTemplate).getForEntity(any(String.class), any(Class.class));

        // Act
        boolean result = adaptorClient.isHealthy(adaptor);

        // Assert
        assertFalse(result);
    }
}

