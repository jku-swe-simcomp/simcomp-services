package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception.CommandTransformationFailedException;

import java.util.Set;
import java.util.stream.Collectors;
/**
 * This class represents a visitor for the {@link ExecutionCommand} hierarchy.
 * It can be used to transform a command into another command.
 */
public abstract class ExecutionCommandTransformationVisitor implements ExecutionCommandVisitor<ExecutionCommand, Set<ActionType>> {
    /**
     * This method is called when a composite command is visited.
     * @param command The composite command
     * @param actionTypes The supported action-types to which the command should be transformed
     * @return The transformed command
     * @throws CommandTransformationFailedException If the transformation fails
     */
    @Override
    public ExecutionCommand visit(ExecutionCommand.CompositeCommand command, Set<ActionType> actionTypes) throws CommandTransformationFailedException {
        var transformedCommands = command.commands().stream()
                .map(c -> {
                    try {
                        return c.accept(this,actionTypes);
                    } catch (Exception e) {
                        throw new CommandTransformationFailedException(
                                "Transformation of command %s failed for action-types %s with message: %s.".formatted(c.getCorrespondingActionType(), actionTypes, e.getMessage())
                        );
                    }
                })
                .collect(Collectors.toList());
        return new ExecutionCommand.CompositeCommand(transformedCommands);
    }

    /**
     * Default behaviour is to return the command without transformation
     * @param command The command
     * @param actionTypes The action-types to which the command should be transformed
     * @return The command as it is
     */
    @Override
    public ExecutionCommand defaultBehaviour(ExecutionCommand command, Set<ActionType> actionTypes){
        return command;
    }
}
