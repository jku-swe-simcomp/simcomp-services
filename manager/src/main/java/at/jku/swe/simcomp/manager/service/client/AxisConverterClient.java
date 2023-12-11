package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AxisConverterClient {
    private final RestTemplate restTemplate;
    private final String axisConverterEndpoint;

    public AxisConverterClient(RestTemplate restTemplate,
                               @Value("${application.axisconverter.endpoint}") String axisConverterEndpoint) {
        this.restTemplate = restTemplate;
        this.axisConverterEndpoint = axisConverterEndpoint;
    }

    public PoseDTO jointPositionsToPose(List<JointPositionDTO> jointPositions){
        HttpEntity<List<JointPositionDTO>> requestEntity = new HttpEntity<>(jointPositions);
        ResponseEntity<PoseDTO> responseEntity = restTemplate.exchange(
                axisConverterEndpoint + "/api/converter/axisToPose",
                HttpMethod.POST,
                requestEntity,
                PoseDTO.class
        );
        return responseEntity.getBody();
    }
}
