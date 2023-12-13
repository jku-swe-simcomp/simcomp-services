package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExecutionCommandKinematicsVisitor implements ExecutionCommandVisitor<ExecutionCommand, Object> {
    private final KinematicsService kinematicsService;
    public ExecutionCommandKinematicsVisitor(KinematicsService kinematicsService) {
        this.kinematicsService = kinematicsService;
    }

    @Override
    public ExecutionCommand visit(ExecutionCommand.PoseCommand command, Object param){
        try{
            return kinematicsService.poseToComposite(command);
        }catch (Exception e){
            log.warn("Kinematics operation failed with message {}.", e.getMessage());
            return command;
        }
    }

    @Override
    public ExecutionCommand visit(ExecutionCommand.CompositeCommand command, Object param){
        List<ExecutionCommand> transformedCommands = command.commands().stream()
                .map(c -> {
                    try {
                        return c.accept(this, null);
                    } catch (Exception e) {
                        return c;
                    }
                }).toList();
        return new ExecutionCommand.CompositeCommand(transformedCommands);
    }

    @Override
    public ExecutionCommand defaultBehaviour(ExecutionCommand command, Object param){
        return command;
    }
}
