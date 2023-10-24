package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoAdaptorEndpointService implements AdaptorEndpointService {
    private final DemoSessionService demoSessionService;
    public DemoAdaptorEndpointService(DemoSessionService demoSessionService) {
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
    public String getAttributeValue(String attributeName, String sessionId) throws SessionNotValidException {
        demoSessionService.renewSession(sessionId);
        return "42";
    }
}
