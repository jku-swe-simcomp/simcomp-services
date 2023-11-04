package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface AdaptorEndpoint {
    ResponseEntity<String> initSession() throws SessionInitializationFailedException;
    ResponseEntity<String> closeSession(String sessionId) throws SessionNotValidException;
    ResponseEntity<ExecutionResultDTO> executeAction(ExecutionCommand executionCommand, String sessionId) throws Exception;
    ResponseEntity<String> getAttribute(String name, String sessionId) throws SessionNotValidException;
    ResponseEntity<Void> healthCheck();
    ResponseEntity<Void> registerAdaptor() throws ServiceRegistrationFailedException, JsonProcessingException;
    ResponseEntity<Void> unregisterAdaptor() throws ServiceRegistrationFailedException;
}
