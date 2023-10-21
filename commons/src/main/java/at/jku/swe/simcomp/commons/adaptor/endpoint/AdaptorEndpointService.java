package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdaptorEndpointService {
    ExecutionResultDTO executeAction(ExecutionCommandDTO command, String sessionId) throws SessionNotValidException;
    ExecutionResultDTO executeSequence(List<ExecutionCommandDTO> executionCommands, String sessionId) throws SessionNotValidException;
    String getAttributeValue(String attributeName, String sessionId) throws SessionNotValidException;
    String initSession() throws SessionInitializationFailedException;
    void closeSession(String sessionId) throws SessionNotValidException;
}
