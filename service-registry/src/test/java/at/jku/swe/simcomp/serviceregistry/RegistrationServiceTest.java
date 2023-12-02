package at.jku.swe.simcomp.serviceregistry;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorStatus;
import at.jku.swe.simcomp.serviceregistry.domain.repository.AdaptorRepository;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import at.jku.swe.simcomp.serviceregistry.service.AdaptorMapper;
import at.jku.swe.simcomp.serviceregistry.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private AdaptorRepository adaptorRepository;

    @Mock
    private AdaptorMapper adaptorMapper;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void testRegister_Success() throws AdaptorAlreadyRegisteredException {
        // Arrange
        ServiceRegistrationConfigDTO config = new ServiceRegistrationConfigDTO();
        config.setName("TestAdaptor");

        Adaptor mockedAdaptor = new Adaptor();
        when(adaptorRepository.findById("TestAdaptor")).thenReturn(Optional.empty());
        when(adaptorMapper.dtoToEntity(config)).thenReturn(mockedAdaptor);

        // Act
        registrationService.register(config);

        // Assert
        verify(adaptorRepository, times(1)).findById("TestAdaptor");
        verify(adaptorRepository, times(1)).save(mockedAdaptor);
        verify(adaptorMapper, times(1)).dtoToEntity(config);
    }

    @Test
    public void testRegister_AdaptorAlreadyRegisteredException() {
        // Arrange
        ServiceRegistrationConfigDTO config = new ServiceRegistrationConfigDTO();
        config.setName("TestAdaptor");

        when(adaptorRepository.findById("TestAdaptor")).thenReturn(Optional.of(new Adaptor()));

        // Act and Assert
        assertThrows(AdaptorAlreadyRegisteredException.class, () -> {
            registrationService.register(config);
        });

        // Verify that findById is called once
        verify(adaptorRepository, times(1)).findById("TestAdaptor");

        // Verify that save and dtoToEntity are not called
        verify(adaptorRepository, never()).save(any());
        verify(adaptorMapper, never()).dtoToEntity(any());
    }

    @Test
    public void testUnregister_Success() {
        // Arrange
        String adaptorId = "TestAdaptor";

        // Act
        registrationService.unregister(adaptorId);

        // Assert
        verify(adaptorRepository, times(1)).deleteById(adaptorId);
        verify(adaptorMapper, times(0)).entityToDto(any()); // No mapping should be involved
    }

    @Test
    public void testGetAllHealthyRegisteredAdaptors_Success() {
        // Arrange
        Adaptor healthyAdaptor1 = new Adaptor();
        healthyAdaptor1.setStatus(AdaptorStatus.HEALTHY);

        Adaptor healthyAdaptor2 = new Adaptor();
        healthyAdaptor2.setStatus(AdaptorStatus.HEALTHY);

        Adaptor unhealthyAdaptor = new Adaptor();
        unhealthyAdaptor.setStatus(AdaptorStatus.UNHEALTHY);

        List<Adaptor> allAdaptors = Arrays.asList(healthyAdaptor1, healthyAdaptor2, unhealthyAdaptor);

        when(adaptorRepository.findAll()).thenReturn(allAdaptors);
        when(adaptorMapper.entityToDto(healthyAdaptor1)).thenReturn(new ServiceRegistrationConfigDTO());
        when(adaptorMapper.entityToDto(healthyAdaptor2)).thenReturn(new ServiceRegistrationConfigDTO());

        // Act
        List<ServiceRegistrationConfigDTO> result = registrationService.getAllHealthyRegisteredAdaptors();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllHealthyRegisteredAdaptors_EmptyList() {
        // Arrange
        when(adaptorRepository.findAll()).thenReturn(List.of());

        // Act
        List<ServiceRegistrationConfigDTO> result = registrationService.getAllHealthyRegisteredAdaptors();

        // Assert
        assertEquals(0, result.size());
    }
}


