package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;

import java.io.IOException;
import org.json.simple.parser.ParseException;

public interface AdaptorEndpointService {
    AttributeValue getAttributeValue(AttributeKey attribute, String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException;
    String initSession() throws SessionInitializationFailedException;
    String initSession(String instanceId) throws SessionInitializationFailedException;
    void closeSession(String sessionId) throws SessionNotValidException;
}
