package at.jku.swe.simcomp.serviceregistry;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorStatus;
import at.jku.swe.simcomp.serviceregistry.domain.model.SupportedActionType;
import at.jku.swe.simcomp.serviceregistry.service.AdaptorMapper;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdaptorMapperTest {

    @Test
    public void testEntityToDto() {
        // Arrange
        Adaptor adaptor = new Adaptor();
        adaptor.setName("TestAdaptor");
        adaptor.setHost("localhost");
        adaptor.setPort(8080);
        adaptor.setStatus(AdaptorStatus.HEALTHY);

        SupportedActionType actionType1 = new SupportedActionType();
        actionType1.setActionType(ActionType.GRAB);
        actionType1.setAdaptor(adaptor);

        SupportedActionType actionType2 = new SupportedActionType();
        actionType2.setActionType(ActionType.CALIBRATE);
        actionType2.setAdaptor(adaptor);

        adaptor.setSupportedActions(List.of(actionType1, actionType2));

        AdaptorMapper adaptorMapper = new AdaptorMapper();

        // Act
        ServiceRegistrationConfigDTO configDTO = adaptorMapper.entityToDto(adaptor);

        // Assert
        assertEquals("TestAdaptor", configDTO.getName());
        assertEquals("localhost", configDTO.getHost());
        assertEquals(8080, configDTO.getPort());
        assertEquals(new HashSet<>(Set.of(ActionType.CALIBRATE, ActionType.GRAB)), configDTO.getSupportedActions());
    }

    @Test
    public void testDtoToEntity() {
        // Arrange
        ServiceRegistrationConfigDTO configDTO = new ServiceRegistrationConfigDTO();
        configDTO.setName("TestAdaptor");
        configDTO.setHost("localhost");
        configDTO.setPort(8080);
        configDTO.setSupportedActions(new HashSet<>(Set.of(ActionType.GRAB, ActionType.CALIBRATE)));

        AdaptorMapper adaptorMapper = new AdaptorMapper();

        // Act
        Adaptor adaptor = adaptorMapper.dtoToEntity(configDTO);

        // Assert
        assertEquals("TestAdaptor", adaptor.getName());
        assertEquals("localhost", adaptor.getHost());
        assertEquals(8080, adaptor.getPort());
        assertEquals(AdaptorStatus.HEALTHY, adaptor.getStatus());

        Set<ActionType> actionTypes = adaptor.getSupportedActions().stream()
                .map(SupportedActionType::getActionType)
                .collect(Collectors.toSet());

        assertEquals(new HashSet<>(Set.of(ActionType.GRAB, ActionType.CALIBRATE)), actionTypes);
    }
}

