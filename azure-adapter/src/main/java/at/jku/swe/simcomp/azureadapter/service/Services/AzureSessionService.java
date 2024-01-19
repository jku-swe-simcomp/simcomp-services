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

/**
 * The AzureSessionService class provides methods for managing Azure simulation sessions.
 */
@Service
@Slf4j
public class AzureSessionService implements AzureSimulationRemovalListener {

    private static final ConcurrentHashMap<String, SimulationInstanceConfig> currentSessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Thread> sessionTerminationThreads = new ConcurrentHashMap<>();
    private static final ExecutorService sessionTerminationExecutor = Executors.newFixedThreadPool(10);
    private static final Long sessionTerminateAfter = 600_000L; // 10 minutes

    /**
     * Constructs an AzureSessionService with the specified AzureSimulationInstanceService dependency.
     *
     * @param azureSimulationInstanceService The AzureSimulationInstanceService to listen for simulation removal events.
     */
    public AzureSessionService(AzureSimulationInstanceService azureSimulationInstanceService) {
        azureSimulationInstanceService.addSimulationRemovalListener(this);
    }

    /**
     * Initializes a new session and returns the session key.
     *
     * @return The key of the newly initialized session.
     * @throws SessionInitializationFailedException If the session initialization fails.
     */
    public synchronized String initializeSession() throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getAvailableInstance();
        return initializeSession(config);
    }

    /**
     * Initializes a new session for the specified instanceId and returns the session key.
     *
     * @param instanceId The ID of the simulation instance.
     * @return The key of the newly initialized session.
     * @throws SessionInitializationFailedException If the session initialization fails.
     */
    public synchronized String initializeSession(String instanceId) throws SessionInitializationFailedException {
        Optional<SimulationInstanceConfig> config = getInstance(instanceId);
        return initializeSession(config);
    }

    /**
     * Initializes a new session with the provided config and returns the session key.
     *
     * @param config The configuration for the new session.
     * @return The key of the newly initialized session.
     * @throws SessionInitializationFailedException If the session initialization fails.
     */
    public synchronized String initializeSession(Optional<SimulationInstanceConfig> config) throws SessionInitializationFailedException {
        if (config.isEmpty()) {
            throw new SessionInitializationFailedException("Simulation instance not available");
        }

        String sessionKey = UUID.randomUUID().toString();
        currentSessions.put(sessionKey, config.get());

        startSessionTerminationThread(sessionKey);
        return sessionKey;
    }

    /**
     * Renews the specified session by restarting its termination thread.
     *
     * @param sessionKey The key of the session to be renewed.
     * @return The renewed SimulationInstanceConfig.
     * @throws SessionNotValidException If the specified session is not valid.
     */
    public synchronized SimulationInstanceConfig renewSession(String sessionKey) throws SessionNotValidException {
        if (!currentSessions.containsKey(sessionKey))
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));

        if (sessionTerminationThreads.containsKey(sessionKey)) {
            sessionTerminationThreads.get(sessionKey).interrupt();
        }

        startSessionTerminationThread(sessionKey);

        log.info("Session {} renewed", sessionKey);
        return currentSessions.get(sessionKey);
    }

    /**
     * Closes the specified session by stopping its termination thread and removing it from the active sessions.
     *
     * @param sessionKey The key of the session to be closed.
     * @throws SessionNotValidException If the specified session is not valid.
     */
    public synchronized void closeSession(String sessionKey) throws SessionNotValidException {
        if (sessionTerminationThreads.containsKey(sessionKey)) {
            sessionTerminationThreads.get(sessionKey).interrupt();
        }

        if (currentSessions.containsKey(sessionKey)) {
            currentSessions.remove(sessionKey);
            log.info("Session {} removed", sessionKey);
        } else {
            throw new SessionNotValidException("Session %s not valid".formatted(sessionKey));
        }
    }

    /**
     * Handles the removal of a simulation instance by closing the associated session if active.
     *
     * @param config The configuration of the removed simulation instance.
     */
    @Override
    public synchronized void onSimulationRemoved(SimulationInstanceConfig config) {
        log.info("Received notification about removal of simulation: {}", config);
        for (var entry : currentSessions.entrySet()) {
            if (entry.getValue().equals(config)) {
                try {
                    closeSession(entry.getKey());
                } catch (SessionNotValidException e) {
                    log.warn("Could not close session upon removal of simulation: {}", config);
                }
                return;
            }
        }
    }

    /**
     * Starts a thread to handle the termination of a session after a specified duration.
     *
     * @param sessionKey The key of the session to be terminated.
     */
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

    /**
     * Finds an available simulation instance that is not currently in use by an active session.
     *
     * @return An Optional containing the first available simulation instance, or an empty Optional if none is available.
     */
    private Optional<SimulationInstanceConfig> getAvailableInstance() {
        return AzureSimulationInstanceService.instances.stream()
                .filter(simulation -> !currentSessions.containsValue(simulation))
                .findFirst();
    }


    /**
     * Finds a specific simulation instance by its instanceId that is not currently in use by an active session.
     *
     * @param instanceId The instanceId of the simulation instance to find.
     * @return An Optional containing the found simulation instance, or an empty Optional if not found or in use.
     */
    private Optional<SimulationInstanceConfig> getInstance(String instanceId) {
        return AzureSimulationInstanceService.instances.stream()
                .filter(simulation -> !currentSessions.containsValue(simulation) && simulation.getInstanceId().equals(instanceId))
                .findFirst();
    }
}