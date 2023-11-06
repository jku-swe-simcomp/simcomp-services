package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception.CommandTransformationFailedException;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class ExecutionCommandTransformationVisitor implements ExecutionCommandVisitor<ExecutionCommand, Set<ActionType>> {
    @Override
    public ExecutionCommand visit(ExecutionCommand.CompositeCommand command, Set<ActionType> actionTypes) throws CommandTransformationFailedException {
        var transformedCommands = command.commands().stream()
                .map(c -> {
                    try {
                        return c.accept(this,actionTypes);
                    } catch (Exception e) {
                        throw new CommandTransformationFailedException(
                                "Validation of command %s failed for action-types %s with message: %s.".formatted(c.getCorrespondingActionType(), actionTypes, e.getMessage())
                        );
                    }
                })
                .collect(Collectors.toList());
        return new ExecutionCommand.CompositeCommand(transformedCommands);
    }

    @Override
    public ExecutionCommand defaultBehaviour(ExecutionCommand command, Set<ActionType> actionTypes){
        return command;
    }
}
