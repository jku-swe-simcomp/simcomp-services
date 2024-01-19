package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

/**
 * The AzureAdaptorEndpointService class implements the AdaptorEndpointService interface
 * to provide Azure-specific endpoint services for simulation instances.
 */
@Service
public class AzureAdaptorEndpointService implements AdaptorEndpointService {

    private final AzureSessionService azureSessionService;

    /**
     * Constructs an AzureAdaptorEndpointService with the specified AzureSessionService.
     *
     * @param azureSessionService The AzureSessionService responsible for managing Azure sessions.
     */
    public AzureAdaptorEndpointService(AzureSessionService azureSessionService) {
        this.azureSessionService = azureSessionService;
    }

    /**
     * Initializes a new session and returns the session ID.
     *
     * @return The ID of the newly initialized session.
     * @throws SessionInitializationFailedException If the session initialization fails.
     */
    @Override
    public String initSession() throws SessionInitializationFailedException {
        return azureSessionService.initializeSession();
    }

    /**
     * Initializes a new session for a specific instance and returns the session ID.
     *
     * @param instanceId The ID of the simulation instance.
     * @return The ID of the newly initialized session.
     * @throws SessionInitializationFailedException If the session initialization fails.
     */
    @Override
    public String initSession(String instanceId) throws SessionInitializationFailedException {
        return azureSessionService.initializeSession(instanceId);
    }

    /**
     * Closes the specified session.
     *
     * @param sessionId The ID of the session to be closed.
     * @throws SessionNotValidException If the specified session is not valid.
     */
    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        azureSessionService.closeSession(sessionId);
    }

    /**
     * Retrieves the attribute value for a specific attribute key in the context of a session.
     *
     * @param attributeKey The attribute key for which to retrieve the value.
     * @param sessionId    The ID of the session in which the attribute value is requested.
     * @return The attribute value corresponding to the provided attribute key.
     * @throws SessionNotValidException If the specified session is not valid.
     */
    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException {
        var config = azureSessionService.renewSession(sessionId);

        return switch (attributeKey) {
            case JOINT_POSITIONS -> new AttributeValue.JointPositions(AzureExecutionService.getAllJointAngles(config.getInstanceId()));
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}