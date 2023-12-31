package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Class to execute a set joint position command
 */
@Slf4j
@Service
public class SetJointExecutor
        implements CommandExecutor<ExecutionCommand.SetJointPositionCommand, ExecutionResultDTO> {

    /**
     * Method that executes a set joint position command at a simulation
     * @param command the command to execute
     * @param config the configuration of the simulation
     * @return the result of the execution
     * @throws RoboOperationFailedException if the simulation could not execute the command
     * @throws IOException if no connection to the simulation could be built
     * @throws ParseException if the created json is invalid
     */
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, SimulationInstanceConfig config)
            throws RoboOperationFailedException, IOException, ParseException {

        System.out.println("Connecting to " + config.getInstanceHost() + " on port " + config.getInstancePort());

        JSONObject json = new JSONObject();
        @NonNull JointPositionDTO movement = command.jointPosition();
        log.info("Executing joint adjustment for axis {} by {} radians", movement.getJoint(), movement.getRadians());
        json.put("operation", "set_axis");
        json.put("axis", movement.getJoint().getIndex());
        json.put("value", movement.getRadians());
        return WebotsExecutionService.executeCommand(json, config);
    }
}


