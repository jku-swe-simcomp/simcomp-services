package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

/**
 * This interface defines the methods for the adaptor endpoint service.
 * Has to be implemented by the simulation adaptor.
 */
public interface AdaptorEndpointService {
    AttributeValue getAttributeValue(AttributeKey attribute, String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException;
    String initSession() throws SessionInitializationFailedException;
    String initSession(String instanceId) throws SessionInitializationFailedException;
    void closeSession(String sessionId) throws SessionNotValidException;
    default List<String> getSupportedCustomCommandTypes(){return List.of();}
    default String getCustomCommandTypeExampleJson(String type){
        return "";
    }
}
