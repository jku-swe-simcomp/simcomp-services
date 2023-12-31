package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.ExecutionCommandValidationVisitor;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponse;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionRepository;
import at.jku.swe.simcomp.manager.domain.repository.ExecutionResponseRepository;
import at.jku.swe.simcomp.manager.rest.exception.CommandExecutionFailedException;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AsyncCommandDistributionService {
    private final AdaptorClient adaptorClient;
    private final ExecutionRepository executionRepository;
    private final ExecutionResponseRepository executionResponseRepository;
    private final AdaptorSessionRepository adaptorSessionRepository;
    private final ExecutionCommandValidationVisitor executionCommandValidationVisitor;

    public AsyncCommandDistributionService(ExecutionRepository executionRepository,
                                           AdaptorClient adaptorClient,
                                           ExecutionResponseRepository executionResponseRepository,
                                           AdaptorSessionRepository adaptorSessionRepository,
                                           ExecutionCommandValidationVisitor executionCommandValidationVisitor) {
        this.executionRepository = executionRepository;
        this.adaptorClient = adaptorClient;
        this.executionResponseRepository = executionResponseRepository;
        this.adaptorSessionRepository = adaptorSessionRepository;
        this.executionCommandValidationVisitor = executionCommandValidationVisitor;
    }
    @Async
    public void distributeCommand(@NonNull Long adaptorSessionId,
                                  @NonNull UUID executionId,
                                  @NonNull ServiceRegistrationConfigDTO serviceRegistrationConfig,
                                  @NonNull ExecutionCommand command) {
        AdaptorSession adaptorSession = adaptorSessionRepository.findById(adaptorSessionId)
                .orElseThrow();

        if(adaptorSession.getState().equals(SessionState.CLOSED)){
            log.info("Ignoring closed adaptor-session {}", adaptorSession.getId());
            return;
        }

        Long responseId = initDefaultExecutionResponse(adaptorSession, executionId);

        try {
            if(Boolean.FALSE.equals(command.accept(executionCommandValidationVisitor, serviceRegistrationConfig.getSupportedActions()))){
                log.warn("Command {} not supported by {}. Aborting.", command, serviceRegistrationConfig);
                updateExecutionResponse(responseId, 400, "Command was not even distributed to simulation as the simulation indicated it does not support at least one of the attempted actions.", ExecutionResponseState.ERROR);
                return;
            }
        } catch (Exception e) {
            log.info("Command validation threw exception: {}. Continuing with distribution nevertheless..", e.getMessage());
        }

        log.info("Distributing command {} to adaptor-session {}", executionId, adaptorSession.getAdaptorName());
        try {
            sendCommandAndUpdateResponse(responseId, serviceRegistrationConfig, command, adaptorSession.getSessionKey());
        } catch (SessionNotValidException e) {
            log.info("Marking simulation session %s from aggregate session %s as closed, as it returned 401 unauthorized.".formatted(adaptorSession.getAdaptorName(), adaptorSession.getSession().getSessionKey()));
            adaptorSessionRepository.updateSessionStateById(adaptorSessionId, SessionState.CLOSED);
        }
    }

    // private region methods

    private void sendCommandAndUpdateResponse(Long responseId,
                                              ServiceRegistrationConfigDTO serviceRegistrationConfig,
                                              ExecutionCommand command,
                                              String adaptorSessionKey) throws SessionNotValidException {
        try{
            ExecutionResultDTO executionResult = adaptorClient.executeCommand(serviceRegistrationConfig, command, adaptorSessionKey);
            updateExecutionResponse(responseId, 200, executionResult.getReport(), ExecutionResponseState.SUCCESS);
            log.info("Execution of command for adaptor-session {} finished: {}. ", serviceRegistrationConfig.getName(), executionResult);
        }catch (CommandExecutionFailedException e){
            log.error("Execution of command failed for adaptor-session {}", serviceRegistrationConfig.getName());
            updateExecutionResponse(responseId, e.getCode(), e.getMessage(), ExecutionResponseState.ERROR);
            if(e.getCode() == 401){
                throw new SessionNotValidException("");
            }
        }catch(Exception e){
            log.error("Execution of command failed for adaptor-session {}", serviceRegistrationConfig.getName());
            updateExecutionResponse(responseId, 500, e.getMessage(), ExecutionResponseState.ERROR);
        }
    }

    private Long initDefaultExecutionResponse(AdaptorSession adaptorSession,
                                                           UUID executionId) {
        ExecutionResponse executionResponse = ExecutionResponse.builder()
                .adaptorSession(adaptorSession)
                .execution(executionRepository.findByExecutionUUIDOrElseThrow(executionId))
                .state(ExecutionResponseState.RUNNING)
                .build();
        executionResponse = executionResponseRepository.save(executionResponse);
        executionRepository.flush();
        return executionResponse.getId();
    }

    private void updateExecutionResponse(Long executionResponseId,
                                         Integer code,
                                         String message,
                                         ExecutionResponseState state) {
        executionResponseRepository.updateExecutionResponse(executionResponseId, code, state, message);
    }

}
