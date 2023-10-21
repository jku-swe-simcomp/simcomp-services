package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdaptorClient {
    private final RestTemplate restTemplate;

    public AdaptorClient() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isHealthy(Adaptor adaptor){
        String url = getHealthCheckUrl(adaptor);
        try {
            restTemplate.getForEntity(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getHealthCheckUrl(Adaptor adaptor){
        return "http://" + adaptor.getHost() + ":" + adaptor.getPort() + AdaptorEndpointConstants.HEALTH_CHECK_PATH;
    }
}
