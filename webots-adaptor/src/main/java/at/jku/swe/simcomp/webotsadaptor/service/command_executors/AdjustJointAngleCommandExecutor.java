package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsExecutionService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import org.json.simple.JSONObject;

import java.io.IOException;

@Slf4j
@Service
public class AdjustJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config)
            throws RoboOperationFailedException, IOException, ParseException {

        JSONObject json = new JSONObject();
        @NonNull JointAngleAdjustmentDTO movement = command.jointAngleAdjustment();
        log.info("Executing joint adjustment for axis {} by {} radians", movement.getJoint(), movement.getByRadians());
        json.put("operation", "adjust_axis");
        json.put("axis", movement.getJoint().getIndex());
        json.put("value", movement.getByRadians());
        return WebotsExecutionService.executeCommand(json, config);
    }
}
