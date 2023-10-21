package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdaptorEndpoint {
    ResponseEntity<String> initSession() throws SessionInitializationFailedException;
    ResponseEntity<String> closeSession(String sessionId) throws SessionNotValidException;
    ResponseEntity<ExecutionResultDTO> executeAction(ExecutionCommandDTO executionCommandDTO, String sessionId) throws SessionNotValidException;
    ResponseEntity<ExecutionResultDTO> executeSequence(List<ExecutionCommandDTO> executionCommands, String sessionId) throws SessionNotValidException;
    ResponseEntity<String> getAttribute(String name, String sessionId) throws SessionNotValidException;
    ResponseEntity<Void> healthCheck();
    ResponseEntity<Void> registerAdaptor() throws ServiceRegistrationFailedException, JsonProcessingException;
    ResponseEntity<Void> unregisterAdaptor() throws ServiceRegistrationFailedException;
}
