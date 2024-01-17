package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Client for performing health checks on adaptors.
 */
@Service
@Slf4j
public class AdaptorClient {
    private final RestTemplate restTemplate;

    /**
     * Constructor
     * @param restTemplate the injected rest template
     */
    public AdaptorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Performs a health check on the given adaptor.
     * @param adaptor the adaptor to check
     * @return true if the health check was successful, false otherwise
     */
    public boolean isHealthy(Adaptor adaptor){
        String url = getHealthCheckUrl(adaptor);
        log.info("Performing health check on adaptor %s with url %s".formatted(adaptor.getName(), url));
        try {
            restTemplate.getForEntity(url, String.class);
            log.info("Health check finished. Result is true");
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    private String getHealthCheckUrl(Adaptor adaptor){
        return "http://" + adaptor.getHost() + ":" + adaptor.getPort() + AdaptorEndpointConstants.HEALTH_CHECK_PATH;
    }
}
