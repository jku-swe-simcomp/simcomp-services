package at.jku.swe.simcomp.manager;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.manager.service.client.AxisConverterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Profile("test")
class AxisConverterClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AxisConverterClient axisConverterClient;


    @Test
    void jointPositionsToPose() {
        // Arrange
        List<JointPositionDTO> jointPositions = Collections.singletonList(new JointPositionDTO());
        PoseDTO expectedPose = new PoseDTO();
        ResponseEntity<PoseDTO> responseEntity = ResponseEntity.ok(expectedPose);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(PoseDTO.class)
        )).thenReturn(responseEntity);

        // Act
        PoseDTO actualPose = axisConverterClient.directKinematics(jointPositions);

        // Assert
        assertEquals(expectedPose, actualPose);
    }
}


