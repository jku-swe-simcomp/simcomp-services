package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Class to build and communication endpoint for the session management.
 * Interaction with a SessionService instance to manage the sessions.
 * @see SessionService
 */
@Service
public class WebotsAdaptorEndpointService implements AdaptorEndpointService {
    private final SessionService SESSION_SERVICE;

    /**
     * Constructor to create a new endpoint for a session service class.
     * @param sessionService the service to keep manage the session
     */
    public WebotsAdaptorEndpointService(SessionService sessionService) {
        this.SESSION_SERVICE = sessionService;
    }

    /**
     * Method to initialize an arbitrary new session.
     * @return the ID of the new session.
     * @throws SessionInitializationFailedException if the session cannot be initialized
     */
    @Override
    public String initSession() throws SessionInitializationFailedException {
        return SESSION_SERVICE.initializeSession();
    }

    /**
     * Method to initialize a new session with a defined instanceID
     * @param instanceId for the new session
     * @return the ID of the new session
     * @throws SessionInitializationFailedException if the session cannot be initialized
     */
    @Override
    public String initSession(String instanceId) throws SessionInitializationFailedException {
        return SESSION_SERVICE.initializeSession(instanceId);
    }

    /**
     * Method to close a session
     * @param sessionId the ID of the session to close
     * @throws SessionNotValidException if the ID no valid session is found
     */
    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        SESSION_SERVICE.closeSession(sessionId);
    }

    /**
     * Method to get an attribute for a session. Currently, only the axes values
     * are available.
     * @param attributeKey the value that is queried for the session
     * @param sessionId the ID of the requested session
     * @return the value of the request attribute
     * @throws SessionNotValidException if the session is not valid
     * @throws RoboOperationFailedException if the request cannot be executed at the simulation
     * @throws IOException if the connection to the simulation cannot be built
     * @throws ParseException if the JSON of the simulation query is not valid
     */
    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        SimulationInstanceConfig config = SESSION_SERVICE.getConfigForSession(sessionId);
        // Note: can add more cases for different attributes
        return switch(attributeKey){
            case JOINT_POSITIONS ->  new AttributeValue.JointPositions(WebotsExecutionService.getPositions(config));
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}
