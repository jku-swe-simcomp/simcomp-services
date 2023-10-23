package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoAdaptorEndpointService implements AdaptorEndpointService {
    private final CommandExecutorDelegate commandExecutorDelegate;
    private final DemoSessionService demoSessionService;
    public DemoAdaptorEndpointService(CommandExecutorDelegate commandExecutorDelegate, DemoSessionService demoSessionService) {
        this.commandExecutorDelegate = commandExecutorDelegate;
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

    @Override
    public ExecutionResultDTO executeSequence(List<ExecutionCommandDTO> executionCommands, String sessionId) throws SessionNotValidException {
        for(ExecutionCommandDTO command : executionCommands){
            executeAction(command, sessionId);
        }
        return getDemoResult("Executed sequence");
    }

    @Override
    public ExecutionResultDTO executeAction(ExecutionCommandDTO command, String sessionId) throws SessionNotValidException {
        DemoSimulationConfig demoSimulationConfig = demoSessionService.renewSession(sessionId);
        return commandExecutorDelegate.execute(command, demoSimulationConfig);
    }

    private ExecutionResultDTO getDemoResult(String message){
        return ExecutionResultDTO.builder()
                .message(message)
                .success(true)
                .build();
    }
}
