package at.jku.swe.simpcomp.azureadapter.config_tests;

import at.jku.swe.simcomp.azureadapter.config.ServiceRegistrationConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class ServiceRegistrationConfigTest {

    @InjectMocks
    private ServiceRegistrationConfig serviceRegistrationConfig;

    @Mock
    @Value("${adaptor.endpoint.name}")
    private String name;

    @Mock
    @Value("${adaptor.endpoint.host}")
    private String host;

    @Mock
    @Value("${server.port}")
    private Integer port;

    @Test
    public void testGetServiceRegistrationConfig() {

        when(name).thenReturn("TestAdaptor");
        when(host).thenReturn("localhost");
        when(port).thenReturn(1234);

        ServiceRegistrationConfigDTO configDTO = serviceRegistrationConfig.getServiceRegistrationConfig(name, host, port);

        assertEquals("TestAdaptor", configDTO.getName());
        assertEquals("localhost", configDTO.getHost());
        assertEquals(1234, configDTO.getPort());
        assertEquals(Set.of(ActionType.SET_JOINT_POSITION, ActionType.ADJUST_JOINT_ANGLE), configDTO.getSupportedActions());
    }
}
