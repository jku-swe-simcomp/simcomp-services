package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebotsAdaptorEndpointService implements AdaptorEndpointService {
    private final WebotsSessionService demoSessionService;
    public WebotsAdaptorEndpointService(WebotsSessionService demoSessionService) {
        this.demoSessionService = demoSessionService;
    }

    @Override
    public String initSession() throws SessionInitializationFailedException {
        return demoSessionService.initializeSession();
    }

    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        demoSessionService.closeSession(sessionId);
    }

    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException {
        demoSessionService.renewSession(sessionId);
        // Note: can add more cases for different attributes
        return switch(attributeKey){
            case JOINT_POSITIONS -> new AttributeValue.JointPositions(List.of()); // TODO: implement logic to fetch positions
            case JOINT_STATES -> new AttributeValue.JointPositions(List.of()); // TODO: implement logic to fetch states
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}
