package at.jku.swe.simcomp.webotsadaptor.config;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ServiceRegistrationConfigTest {

    @Test
    void getServiceRegistrationConfigTest(){

        ServiceRegistrationConfigDTO config = new ServiceRegistrationConfig()
                .getServiceRegistrationConfig(
                        "name",
                        "host",
                        1234);

        assertEquals(config.getPort(), 1234);
        assertEquals(config.getHost(), "host");
        assertEquals(config.getName(), "name");
        Set<ActionType> actions = config.getSupportedActions();
        assertTrue(actions.contains(ActionType.POSE));
        assertTrue(actions.contains(ActionType.ADJUST_JOINT_ANGLE));
        assertTrue(actions.contains(ActionType.RESET_TO_HOME));
        assertTrue(actions.contains(ActionType.GRAB));
        assertTrue(actions.contains(ActionType.OPEN_HAND));
        assertTrue(actions.contains(ActionType.SET_JOINT_POSITION));
    }
}
