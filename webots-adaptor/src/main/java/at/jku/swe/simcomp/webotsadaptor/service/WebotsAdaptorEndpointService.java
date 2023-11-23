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

@Service
public class WebotsAdaptorEndpointService implements AdaptorEndpointService {
    private final SessionService sessionService;
    public WebotsAdaptorEndpointService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String initSession() throws SessionInitializationFailedException {
        return sessionService.initializeSession();
    }

    @Override
    public void closeSession(String sessionId) throws SessionNotValidException {
        sessionService.closeSession(sessionId);
    }

    @Override
    public AttributeValue getAttributeValue(AttributeKey attributeKey, String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        SimulationInstanceConfig config = sessionService.getConfigForSession(sessionId);
        // Note: can add more cases for different attributes
        return switch(attributeKey){
            case JOINT_POSITIONS ->  new AttributeValue.JointPositions(WebotsExecutionService.getPositions(config));
            default -> throw new UnsupportedOperationException("Attribute %s not supported by this service".formatted(attributeKey));
        };
    }
}
