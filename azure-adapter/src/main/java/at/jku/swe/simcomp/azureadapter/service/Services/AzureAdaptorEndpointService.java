package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

@Service
public class AzureAdaptorEndpointService implements AdaptorEndpointService {
    private final AzureSessionService demoSessionService;
    public AzureAdaptorEndpointService(AzureSessionService demoSessionService) {
        this.demoSessionService = demoSessionService;
    }

    @Override
    public String initSession() throws SessionInitializationFailedException {
        return demoSessionService.initializeSession();
    }

    @Override
    public String initSession(String instanceId) throws SessionInitializationFailedException {
        return demoSessionService.initializeSession(instanceId);
    }

    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        demoSessionService.closeSession(sessionId);
    }

    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException {
        demoSessionService.renewSession(sessionId);
        // Note: can add more cases for different attributes
        /*
         * TODO: Changes like needed
         */
        return switch(attributeKey){
            case JOINT_POSITIONS -> new AttributeValue.JointPositions(AzureCommandExecutionVisitor.currentJointPositions);
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}
