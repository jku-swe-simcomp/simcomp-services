package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This client is responsible for communicating with the axis-converter service.
 */
@Service
public class AxisConverterClient {
    private final RestTemplate restTemplate;
    private final String axisConverterEndpoint;

    /**
     * Constructor
     * @param restTemplate RestTemplate
     * @param axisConverterEndpoint the endpoint of the axis-converter
     */
    public AxisConverterClient(RestTemplate restTemplate,
                               @Value("${application.axisconverter.endpoint}") String axisConverterEndpoint) {
        this.restTemplate = restTemplate;
        this.axisConverterEndpoint = axisConverterEndpoint;
    }

    /**
     * This method calls the axis-converter service to calculate the direct kinematics.
     * @param jointPositions the joint positions to be transformed
     * @return the pose obtained through direct kinematics
     */
    public PoseDTO directKinematics(List<JointPositionDTO> jointPositions){
        HttpEntity<List<JointPositionDTO>> requestEntity = new HttpEntity<>(jointPositions);
        ResponseEntity<PoseDTO> responseEntity = restTemplate.exchange(
                axisConverterEndpoint + "/api/converter/axisToPose",
                HttpMethod.POST,
                requestEntity,
                PoseDTO.class
        );
        return responseEntity.getBody();
    }

    /**
     * This method calls the axis-converter service to calculate the inverse kinematics.
     * @param poseDTO the pose to be transformed
     * @return the joint positions obtained through inverse kinematics
     */
    public List<JointPositionDTO> inverseKinematics(PoseDTO poseDTO){
        HttpEntity<PoseDTO> requestEntity = new HttpEntity<>(poseDTO);
        ResponseEntity<List<JointPositionDTO>> responseEntity = restTemplate.exchange(
                axisConverterEndpoint + "/api/converter/poseToAxis",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>(){}
        );
        return responseEntity.getBody();
    }
}
