package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

@Service
public class AzureAdaptorEndpointService implements AdaptorEndpointService {
    private final AzureSessionService azureSessionService;
    public AzureAdaptorEndpointService(AzureSessionService azureSessionService) {
        this.azureSessionService = azureSessionService;
    }

    @Override
    public String initSession() throws SessionInitializationFailedException {
        return azureSessionService.initializeSession();
    }

    @Override
    public String initSession(String instanceId) throws SessionInitializationFailedException {
        return azureSessionService.initializeSession(instanceId);
    }

    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        azureSessionService.closeSession(sessionId);
    }

    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException {
        azureSessionService.renewSession(sessionId);

        return switch(attributeKey){
            case JOINT_POSITIONS ->  new AttributeValue.JointPositions(AzureExecutionService.getAllJointAngles("Test"));
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}
