package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.simulation.AzureSimulationRemovalListener;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AzureSessionService implements AzureSimulationRemovalListener {
    private static final ConcurrentHashMap<String, SimulationInstanceConfig> currentSessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Thread> sessionTerminationThreads = new ConcurrentHashMap<>();
    private static final ExecutorService sessionTerminationExecutor = Executors.newFixedThreadPool(10);
    private static final Long sessionTerminateAfter = 600_000L;

    public AzureSessionService(AzureSimulationInstanceService azureSimulationInstanceService){
        azureSimulationInstanceService.addSimulationRemovalListener(this);
    }

    public synchronized String initializeSession() throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getAvailableInstance();
        return initializeSession(config);
    }

    public synchronized String initializeSession(String instanceId) throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getInstance(instanceId);
        return initializeSession(config);
    }

    public synchronized String initializeSession(Optional<SimulationInstanceConfig> config) throws SessionInitializationFailedException {
        if(config.isEmpty()){
            throw new SessionInitializationFailedException("Simulation instance not available");
        }

        String sessionKey =  UUID.randomUUID().toString();
        currentSessions.put(sessionKey, config.get());

        startSessionTerminationThread(sessionKey);
        return sessionKey;
    }

    public synchronized SimulationInstanceConfig renewSession(String sessionKey) throws SessionNotValidException {
        if(!currentSessions.containsKey(sessionKey))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));

        if(sessionTerminationThreads.containsKey(sessionKey)){
            sessionTerminationThreads.get(sessionKey).interrupt();
        }

        startSessionTerminationThread(sessionKey);

        log.info("Session {} renewed", sessionKey);
        return currentSessions.get(sessionKey);
    }

    public synchronized void closeSession (String sessionKey) throws SessionNotValidException {
        if(sessionTerminationThreads.containsKey(sessionKey)) {
            sessionTerminationThreads.get(sessionKey).interrupt();
        }

        if(currentSessions.containsKey(sessionKey)){
            currentSessions.remove(sessionKey);
            log.info("Session {} removed", sessionKey);
        } else {
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));
        }
    }

    @Override
    public synchronized void onSimulationRemoved(SimulationInstanceConfig config) {
        log.info("Received notification about removal of simulation: {}", config);
        for(var entry : currentSessions.entrySet()){
            if(entry.getValue().equals(config)){
                try {
                    closeSession(entry.getKey());
                } catch (SessionNotValidException e) {
                    log.warn("Could not close session upon removal of simulation: {}", config);
                }
                return;
            }
        }
    }

    private void startSessionTerminationThread(String sessionKey) {
        sessionTerminationExecutor.execute(() -> {
            log.info("Session termination {} started", sessionKey);
            log.info("Session termination {} will terminate in {} seconds", sessionKey, sessionTerminateAfter / 1000);
            sessionTerminationThreads.put(sessionKey, Thread.currentThread());
            try {
                Thread.sleep(sessionTerminateAfter);
                currentSessions.remove(sessionKey);
                log.info("Session {} removed by termination thread", sessionKey);
            } catch (InterruptedException e) {
                log.info("Session termination thread {} interrupted", sessionKey);
            }
        });
    }

    private Optional<SimulationInstanceConfig> getAvailableInstance() {
        return AzureSimulationInstanceService.instances.stream()
                .filter(simulation -> !currentSessions.containsValue(simulation))
                .findFirst();
    }

    private Optional<SimulationInstanceConfig> getInstance(String instanceId) {
        return AzureSimulationInstanceService.instances.stream()
                .filter(simulation -> !currentSessions.containsValue(simulation) && simulation.getInstanceId().equals(instanceId))
                .findFirst();
    }

}
