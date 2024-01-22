package at.jku.swe.simcomp.webotsdroneadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.webotsdroneadaptor.domain.simulation.DroneInstanceRemovalListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WebotsSimulationInstanceServiceTest {

    final SimulationInstanceConfig INVALID_CONFIG = new SimulationInstanceConfig(
            "INVALID",
            "SOME_ID",
            "host",
            1234
    );

    final SimulationInstanceConfig VALID_CONFIG_1 = new SimulationInstanceConfig(
            "WEBOTS",
            "SOME_ID",
            "host",
            1234
    );

    final SimulationInstanceConfig VALID_CONFIG_2 = new SimulationInstanceConfig(
            "WEBOTS",
            "SOME_ID",
            "other_host",
            1234
    );

    @Test
    void addSimulationInstanceTest() throws Exception {
        WebotsDroneSimulationInstanceService service = new WebotsDroneSimulationInstanceService("WEBOTS");
        assertThrows(BadRequestException.class, () -> service.addSimulationInstance(INVALID_CONFIG));
        service.addSimulationInstance(VALID_CONFIG_1);
        assertEquals(1, service.getSimulationInstances().size());
        assertThrows(BadRequestException.class, () -> service.addSimulationInstance(VALID_CONFIG_1));
        assertThrows(BadRequestException.class, () -> service.addSimulationInstance(VALID_CONFIG_2));
        service.removeSimulationInstance("SOME_ID");
    }

    @Test
    void removeSimulationInstanceTest() throws Exception {
        WebotsDroneSimulationInstanceService service = new WebotsDroneSimulationInstanceService("WEBOTS");
        service.addSimulationInstance(VALID_CONFIG_1);
        service.removeSimulationInstance("SOME_ID");
        assertEquals(0, WebotsDroneSimulationInstanceService.getInstances().size());
        service.addSimulationInstance(VALID_CONFIG_1);
        service.removeSimulationInstance("INVALID_ID");
        service.removeSimulationInstance("SOME_ID");
    }

    @Test
    void addSimulationRemovalListenerTest() {
        WebotsDroneSimulationInstanceService service = new WebotsDroneSimulationInstanceService("WEBOTS");
        service.addSimulationRemovalListener(new DroneInstanceRemovalListener() {
            @Override
            public void onSimulationRemoved(SimulationInstanceConfig config) {

            }
        });
    }
}
