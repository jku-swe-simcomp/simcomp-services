package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.SimulationInstanceRemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class provides a service to manage sessions
 * @see WebotsSimulationInstanceService
 * @see SimulationInstanceRemovalListener
 */
@Service
@Slf4j
public class SessionService implements SimulationInstanceRemovalListener {
    private static final ConcurrentHashMap<String, SimulationInstanceConfig> CURRENT_SESSIONS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Thread> SESSION_TERMINATION_THREADS = new ConcurrentHashMap<>();
    private static final ExecutorService SESSION_TERMINATION_EXECUTOR = Executors.newFixedThreadPool(10);
    private static final Long SESSION_TERMINATE_AFTER = 600_000L;

    /**
     * Constructor to create a new instance and adds itself as a simulation removal listener
     * to a WebotsSimulationInstanceService instance
     * @param webotsSimulationInstanceService Webots simulation service to which the new
     *                                        instance is added as simulation removal listener
     */
    public SessionService(WebotsSimulationInstanceService webotsSimulationInstanceService){
        webotsSimulationInstanceService.addSimulationRemovalListener(this);
    }

    /**
     * Method to create an arbitrary new session from the pool of available instances
     * @return the key of the new session
     * @throws SessionInitializationFailedException if no session can be created
     */
    public synchronized String initializeSession() throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getAvailableInstance();
        return initializeSession(config);
    }

    /**
     * Method to create a new session for a specfic instance
     * @param instanceId the ID of the instance the new session should be created for
     * @return the key of the new session
     * @throws SessionInitializationFailedException if no session can be created for the instance
     */
    public synchronized String initializeSession(String instanceId) throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getInstance(instanceId);
        return initializeSession(config);
    }

    /**
     * Method to create a new session for an input configuration
     * @param config the configuration for the new session
     * @return the key of the new session
     * @throws SessionInitializationFailedException if no session can be created
     */
    public synchronized String initializeSession(Optional<SimulationInstanceConfig> config) throws SessionInitializationFailedException {
        if(config.isEmpty()){
            throw new SessionInitializationFailedException("No simulation instance available");
        }

        String sessionKey =  UUID.randomUUID().toString();
        CURRENT_SESSIONS.put(sessionKey, config.get());

        startSessionTerminationThread(sessionKey);
        return sessionKey;
    }


    /**
     * Method to renew a session and thereby be rest the timeout countdown
     * @param sessionKey the ID of the session that should be renewed
     * @return the configuration of the renewed session
     * @throws SessionNotValidException if the session key is not found
     */
    public synchronized SimulationInstanceConfig renewSession(String sessionKey) throws SessionNotValidException {
        if(!CURRENT_SESSIONS.containsKey(sessionKey))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));

        if(SESSION_TERMINATION_THREADS.containsKey(sessionKey)){
            SESSION_TERMINATION_THREADS.get(sessionKey).interrupt();
        }

        startSessionTerminationThread(sessionKey);

        log.info("Session {} renewed", sessionKey);
        return CURRENT_SESSIONS.get(sessionKey);
    }

    /**
     * Method to get the configuration of a session based on the session key
     * @param sessionKey the key of the requested session
     * @return the configuration of for the requested session key
     * @throws SessionNotValidException if the session key is not found
     */
    public SimulationInstanceConfig getConfigForSession(String sessionKey) throws SessionNotValidException {
        if(!CURRENT_SESSIONS.containsKey(sessionKey))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));

        return CURRENT_SESSIONS.get(sessionKey);
    }

    /**
     * Method to close a session
     * @param sessionKey the key of the session to close
     * @throws SessionNotValidException if the session key is not found
     */
    public synchronized void closeSession (String sessionKey) throws SessionNotValidException {
        if(SESSION_TERMINATION_THREADS.containsKey(sessionKey)) {
            SESSION_TERMINATION_THREADS.get(sessionKey).interrupt();
        }

        if(CURRENT_SESSIONS.containsKey(sessionKey)){
            CURRENT_SESSIONS.remove(sessionKey);
            log.info("Session {} removed", sessionKey);
        } else {
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));
        }
    }

    /**
     * Method that gets called is a session gets closed. Check if the session to closed
     * is managed by the session service and closes the session if so.
     * @param config the configuration of the session that should be closed
     */
    @Override
    public synchronized void onSimulationRemoved(SimulationInstanceConfig config) {
        log.info("Received notification about removal of simulation: {}", config);
        for(var entry : CURRENT_SESSIONS.entrySet()){
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
        SESSION_TERMINATION_EXECUTOR.execute(() -> {
            log.info("Session termination {} started", sessionKey);
            log.info("Session termination {} will terminate in {} seconds", sessionKey, SESSION_TERMINATE_AFTER / 1000);
            SESSION_TERMINATION_THREADS.put(sessionKey, Thread.currentThread());
            try {
                Thread.sleep(SESSION_TERMINATE_AFTER);
                CURRENT_SESSIONS.remove(sessionKey);
                log.info("Session {} removed by termination thread", sessionKey);
            } catch (InterruptedException e) {
                log.info("Session termination thread {} interrupted", sessionKey);
            }
        });
    }

    private Optional<SimulationInstanceConfig> getAvailableInstance() {
        return WebotsSimulationInstanceService.getInstances().stream()
                .filter(instance -> !CURRENT_SESSIONS.containsValue(instance))
                .findFirst();
    }

    private Optional<SimulationInstanceConfig> getInstance(String instanceId) {
        return WebotsSimulationInstanceService.getInstances().stream()
                .filter(instance -> !CURRENT_SESSIONS.containsValue(instance) && instance.getInstanceId().equals(instanceId))
                .findFirst();
    }
}
