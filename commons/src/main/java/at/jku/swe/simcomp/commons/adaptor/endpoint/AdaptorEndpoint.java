package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * This interface defines the methods for the adaptor endpoint.
 */
public interface AdaptorEndpoint {
    ResponseEntity<String> initSession(String instanceId) throws SessionInitializationFailedException;
    ResponseEntity<String> closeSession(String sessionId) throws SessionNotValidException;
    ResponseEntity<ExecutionResultDTO> executeAction(ExecutionCommand executionCommand, String sessionId) throws Exception;
    ResponseEntity<AttributeValue> getAttribute(AttributeKey attribute, String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException, org.json.simple.parser.ParseException;
    ResponseEntity<List<String>> getCustomCommandTypes();
    ResponseEntity<String> getCustomCommandTypeExampleJson(@PathVariable String type);
    ResponseEntity<Void> healthCheck();
    ResponseEntity<Void> registerAdaptor() throws ServiceRegistrationFailedException, JsonProcessingException;
    ResponseEntity<Void> unregisterAdaptor() throws ServiceRegistrationFailedException;
}
