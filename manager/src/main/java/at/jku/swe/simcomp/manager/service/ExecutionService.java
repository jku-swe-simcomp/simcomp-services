package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionDTO;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.*;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This service is responsible for executing commands {@link ExecutionCommand} for a given session.
 */
@Service
@Slf4j
public class ExecutionService {
    private final AdaptorSessionRepository adaptorSessionRepository;
    private final SessionRepository sessionRepository;
    private final ExecutionRepository executionRepository;
    private final ObjectMapper objectMapper;
    private final AsyncCommandDistributionService commandDistributionService;
    private final ServiceRegistryClient serviceRegistryClient;
    private final ExecutionCommandKinematicsVisitor kinematicsVisitor;
    private final boolean isInverseKinematicsEnabled;

    /**
     * Constructor
     * @param sessionRepository the session repository
     * @param objectMapper the object mapper
     * @param executionRepository the execution repository
     * @param commandDistributionService the command distribution service
     * @param serviceRegistryClient the service registry client
     * @param adaptorSessionRepository the adaptor session repository
     * @param kinematicsVisitor the kinematics visitor
     * @param isInverseKinematicsEnabled the flag to enable inverse kinematic, defaults to false.
     */
    public ExecutionService(SessionRepository sessionRepository,
                            ObjectMapper objectMapper,
                            ExecutionRepository executionRepository,
                            AsyncCommandDistributionService commandDistributionService,
                            ServiceRegistryClient serviceRegistryClient,
                            AdaptorSessionRepository adaptorSessionRepository,
                            ExecutionCommandKinematicsVisitor kinematicsVisitor,
                            @Value("${application.kinematics.inverse.enabled}") Boolean isInverseKinematicsEnabled) {
        this.sessionRepository = sessionRepository;
        this.objectMapper = objectMapper;
        this.executionRepository = executionRepository;
        this.commandDistributionService = commandDistributionService;
        this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorSessionRepository = adaptorSessionRepository;
        this.kinematicsVisitor = kinematicsVisitor;
        this.isInverseKinematicsEnabled = Objects.requireNonNullElse(isInverseKinematicsEnabled, false);
    }

    /**
     * Fetches all executions for a given session.
     * @param sessionKey the session key
     * @return the executions
     */
    public List<ExecutionDTO> getAllExecutionsForSession(UUID sessionKey) {
        return executionRepository.findBySessionSessionKey(sessionKey).stream().map(this::fromModel).toList();
    }

    /**
     * Executes a command for a given session by distributing the commands to the adaptors.
     * If inverse kinematics is enabled, the command is transformed using the kinematics service (axis-converter).
     * @param sessionId the session id
     * @param command the command
     * @return the execution id
     * @throws BadRequestException if the session is closed
     */
    public UUID executeCommand(@NonNull final UUID sessionId, @NonNull ExecutionCommand command) throws BadRequestException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionId);
        if(session.getState().equals(SessionState.CLOSED)){
            throw new BadRequestException("Session %s already closed.".formatted(sessionId));
        }
        log.info("Executing command {} for session {}", command, sessionId);
        Execution execution = initExecution(command, sessionId);
        log.info("Created execution {} for session {}", execution.getExecutionId(), sessionId);
        if(isInverseKinematicsEnabled){
            try {
                command = command.accept(kinematicsVisitor, null);
                log.info("Transformed command using kinematics: {}.", command);
            } catch (Exception e) {
                log.warn("Kinematics operation failed, using original command.");
            }
        }
        distributeCommands(sessionId, execution.getExecutionId(), command);
        return execution.getExecutionId();
    }

    /**
     * Fetches an execution for a given execution id.
     * @param executionId the execution id
     * @return the execution
     */
    public ExecutionDTO getExecution(UUID executionId) {
        Execution execution = executionRepository.findByExecutionUUIDOrElseThrow(executionId);
        return fromModel(execution);
    }


    // private region methods
    private ExecutionDTO fromModel(Execution execution) {
        return new ExecutionDTO(execution.getExecutionId().toString(),
                execution.getSession().getSessionKey().toString(),
                execution.getCommand(),
                execution.getCreatedAt(),
                execution.getResponses().stream()
                        .map(r -> new ExecutionResponseDTO(r.getAdaptorSession().getAdaptorName(), r.getResponseCode(), r.getState(), r.getReport())).toList()
                );
    }

    private Execution initExecution(ExecutionCommand command, @NonNull UUID sessionId) {
        String commandJson = null;
        try {
            commandJson = objectMapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize command to JSON", e);
        }

        Execution execution = Execution.builder()
                .command(commandJson)
                .executionId(UUID.randomUUID())
                .build();

        execution.setSession(sessionRepository.findBySessionKeyOrElseThrow(sessionId));
        executionRepository.save(execution);
        executionRepository.flush();
        return execution;
    }

    private void distributeCommands(UUID sessionId,
                                   UUID executionId,
                                   ExecutionCommand command){
        log.info("Starting to distribute commands..");
        List<ServiceRegistrationConfigDTO> serviceRegistrationConfigs = serviceRegistryClient.getRegisteredAdaptors();
        for(AdaptorSession adaptorSession : adaptorSessionRepository.findBySessionSessionKey(sessionId)){
            if(adaptorSession.getState().equals(SessionState.CLOSED)){
                log.info("Ignoring closed adaptor-session {}", adaptorSession.getId());
                return;
            }
            serviceRegistrationConfigs.stream()
                    .filter(s -> s.getName().equals(adaptorSession.getAdaptorName()))
                    .findFirst()
                    .ifPresentOrElse(s -> commandDistributionService.distributeCommand(adaptorSession.getId(), executionId, s, command),
                            () -> log.warn("Adaptor {} not found in service-registry", adaptorSession.getAdaptorName()));
        }

    }

}
