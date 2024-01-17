package at.jku.swe.simcomp.webotsdroneadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.webotsdroneadaptor.service.command_executors.CustomExecutor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Class to implement the visitor pattern for the supported
 * command types. For each command type a visitor method is
 * implemented, that executes the command with the respective
 * command executor.
 */
@Service
public class WebotsCommandExecutionVisitor extends CommandExecutionVisitor {
    private final SessionService sessionService;
    private final CustomExecutor customExecutor;

    /**
     * Constructor to set all command executors, and the session service
     * @param sessionService session service instance to keep track of the session instances
     * @param customExecutor executor for custom commands
     */
    public WebotsCommandExecutionVisitor(SessionService sessionService, CustomExecutor customExecutor){
        this.sessionService = sessionService;
        this.customExecutor = customExecutor;
    }



    /**
     * Method to call the executor for set joint position commands, with a new command.
     * The execution is executed for a session, the session gets renewed.
     *
     * @param command to set the joint poistion of one axis of the robot arm
     * @param sessionKey the key of the session
     *
     * @return the result of the execution
     * @throws SessionNotValidException if the session is not registered
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the JSON of the command is not valid
     */
    @Override
    public ExecutionResultDTO visit(ExecutionCommand.CustomCommand command, String sessionKey) throws Exception {
        return customExecutor.execute(command, sessionService.renewSession(sessionKey));
    }
}
