package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class AdaptorClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AdaptorClient() {
        // TODO Auto-generated constructor stub
    }

    public Optional<String> getSession(ServiceRegistrationConfigDTO adaptorConfig) {
        String url = "http://" + adaptorConfig.getHost() + ":" + adaptorConfig.getPort() + AdaptorEndpointConstants.INIT_SESSION_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(null), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            } else {
                HttpErrorDTO errorDTO = objectMapper.readValue(response.getBody(), HttpErrorDTO.class);
                log.info("Non-200 response: {}", errorDTO);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error during REST call to initialize adaptor session.", e);
            return Optional.empty();
        }
    }

    public void closeSession(ServiceRegistrationConfigDTO adaptorConfig, String sessionKey){
        String url = "http://" + adaptorConfig.getHost() + ":" + adaptorConfig.getPort() + AdaptorEndpointConstants.getCloseSessionPathForSessionId(sessionKey);
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error during REST call to delete adaptor session.", e);
        }
    }
}
