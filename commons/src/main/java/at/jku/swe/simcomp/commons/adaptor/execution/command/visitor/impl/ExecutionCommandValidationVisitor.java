package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception.CommandValidationFailedException;

import java.util.Set;

public final class ExecutionCommandValidationVisitor implements ExecutionCommandVisitor<Boolean, Set<ActionType>> {
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

    @Override
    public Boolean defaultBehaviour(ExecutionCommand command, Set<ActionType> actionTypes){
        return actionTypes.contains(command.getCorrespondingActionType());
    }
}
