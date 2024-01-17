package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.CompositeCommandExecutionFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.InvalidCommandParametersException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;

/**
 * This class represents a visitor for the {@link ExecutionCommand} hierarchy.
 * Contains a default implementation for the composite command.
 */
public abstract class CommandExecutionVisitor implements ExecutionCommandVisitor<ExecutionResultDTO, String> {
    /**
     * This method is called when a composite command is visited.
     * @param composite The composite command
     * @param sessionKey The session key of the current session
     * @return The result of the execution
     * @throws Exception If the execution fails
     */
    @Override
    public final ExecutionResultDTO visit(ExecutionCommand.CompositeCommand composite, String sessionKey) throws Exception {
        var commands = composite.commands();
        if(commands.isEmpty()){
            throw new InvalidCommandParametersException("The composite command must contain at least one command.");
        }

        ExecutionResultDTO resultDTO = null;
        StringBuilder report = new StringBuilder();
        for(var command: commands){
            resultDTO = tryAcceptSubCommand(command, sessionKey, report);
            report.append(getDecoratedReportForCompositeCommandReport(command, resultDTO.getReport()));
        }
        return setMessageAndReturn(resultDTO, report.toString());
    }

    /**
     * This method is called when a sub-command is visited from a composite command.
     * @param command The sub-command
     * @param sessionKey The session key of the current session
     * @param report The report of the execution up to this point
     * @return The result of the execution
     * @throws CompositeCommandExecutionFailedException If the execution fails
     */
    private ExecutionResultDTO tryAcceptSubCommand(ExecutionCommand command, String sessionKey, StringBuilder report) throws CompositeCommandExecutionFailedException {
        try {
            return command.accept(this, sessionKey);
        } catch(CompositeCommandExecutionFailedException e){// a nested composite-command threw the exception, just prepending the report and rethrowing the exception
            e.prependReport(report.toString());
            throw e;
        } catch (Exception e) {// a scalar execution-command threw the exception, throwing dedicated exception
            String compositeCommandFailedPrefix = "The execution of the composite command threw an exception with message: "
                    + e.getMessage()
                    + "\n"
                    + "Execution reports up to this point were: \n";
            throw new CompositeCommandExecutionFailedException(compositeCommandFailedPrefix, e, report.toString());
        }
    }

    private String getDecoratedReportForCompositeCommandReport(ExecutionCommand command, String report){
       return "The command %s was executed with message: %s. %n".formatted(command.getCorrespondingActionType(), report);
    }

    private ExecutionResultDTO setMessageAndReturn(ExecutionResultDTO resultDTO, String message) {
        resultDTO.setReport(message);
        return resultDTO;
    }
}
