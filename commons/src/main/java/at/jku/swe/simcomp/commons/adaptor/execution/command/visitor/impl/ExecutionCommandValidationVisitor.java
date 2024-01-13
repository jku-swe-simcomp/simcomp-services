package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception.CommandValidationFailedException;

import java.util.Set;

/**
 * This class represents a visitor for the {@link ExecutionCommand} hierarchy.
 * It can be used to validate a command; i.e. determine if it supported according to a set of action-types.
 */
public final class ExecutionCommandValidationVisitor implements ExecutionCommandVisitor<Boolean, Set<ActionType>> {
    /**
     * This method is called when a composite command is visited.
     * @param command The composite command
     * @param actionTypes The supported action-types
     * @return True if the command is supported, false otherwise
     * @throws CommandValidationFailedException If the validation fails
     */
    @Override
    public Boolean visit(ExecutionCommand.CompositeCommand command, Set<ActionType> actionTypes) throws CommandValidationFailedException {
        return command.commands()
                .stream()
                .map(c -> {
                    try {
                        return c.accept(this,actionTypes);
                    } catch (Exception e) {
                        throw new CommandValidationFailedException(
                                "Validation of command %s failed for action-types %s with message: %s.".formatted(c.getCorrespondingActionType(), actionTypes, e.getMessage())
                        );
                    }
                })
                .reduce(true, (a,b) -> a && b);
    }

    /**
     * Default behaviour is to return true if the action-type of the command is contained in the set of supported action-types.
     * @param command The command
     * @param actionTypes The supported action-types
     * @return True if the command is supported, false otherwise
     */
    @Override
    public Boolean defaultBehaviour(ExecutionCommand command, Set<ActionType> actionTypes){
        return actionTypes.contains(command.getCorrespondingActionType());
    }
}
