package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;

public interface AdaptorEndpointService {
    String getAttributeValue(String attributeName, String sessionId) throws SessionNotValidException;
    String initSession() throws SessionInitializationFailedException;
    void closeSession(String sessionId) throws SessionNotValidException;
}
