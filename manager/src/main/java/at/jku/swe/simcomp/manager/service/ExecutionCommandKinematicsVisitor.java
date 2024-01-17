package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This visitor is responsible for transforming pose commands to a composite command containing set-joint-position commands and vice versa
 * with the kinematics offered by the axis converter.
 * All other commands are returned as they are.
 */
@Service
@Slf4j
public class ExecutionCommandKinematicsVisitor implements ExecutionCommandVisitor<ExecutionCommand, Object> {
    private final KinematicsService kinematicsService;

    /**
     * Constructor
     * @param kinematicsService the kinematics service
     */
    public ExecutionCommandKinematicsVisitor(KinematicsService kinematicsService) {
        this.kinematicsService = kinematicsService;
    }

    /**
     * Transforms a pose command to a composite command containing set-joint-position commands.
     * If an error occurs, the original command is returned.
     * @param command the pose command
     * @param param the param which is ignored but required by the visitor interface
     * @return the transformed command
     */
    @Override
    public ExecutionCommand visit(ExecutionCommand.PoseCommand command, Object param){
        try{
            return kinematicsService.poseToComposite(command);
        }catch (Exception e){
            log.warn("Kinematics operation failed with message {}.", e.getMessage());
            return command;
        }
    }

    /**
     * Transforms all commands of a composite command using this visitor.
     * @param command the composite command
     * @param param the param which is ignored but required by the visitor interface
     * @return the transformed command
     */
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

    /**
     * Returns the passed command as it is, which is the default behaviour for commands.
     * @param command the command
     * @param param the param which is ignored but required by the visitor interface
     * @return the command
     */
    @Override
    public ExecutionCommand defaultBehaviour(ExecutionCommand command, Object param){
        return command;
    }
}
