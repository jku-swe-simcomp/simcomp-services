package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorStatus;
import at.jku.swe.simcomp.serviceregistry.domain.repository.AdaptorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckScheduler {
    private final AdaptorRepository adaptorRepository;
    private final AdaptorClient adaptorClient;
    public HealthCheckScheduler(AdaptorRepository adaptorRepository, AdaptorClient adaptorClient) {
       this.adaptorRepository = adaptorRepository;
       this.adaptorClient = adaptorClient;
    }

    @Scheduled(fixedRate = 60000)
    public void performHealthCheckAndSetStatus(){
        log.info("Performing health check on all adaptors");
        adaptorRepository.findAll().forEach(adaptor -> {
            boolean isHealthy = adaptorClient.isHealthy(adaptor);
            if(isHealthy){
                log.info("Adaptor {} is healthy", adaptor.getName());
                adaptor.setStatus(AdaptorStatus.HEALTHY);
            } else {
                log.info("Adaptor {} is unhealthy", adaptor.getName());
                adaptor.setStatus(AdaptorStatus.UNHEALTHY);
            }
            adaptorRepository.save(adaptor);
        });
        log.info("Health check finished");
    }
}
