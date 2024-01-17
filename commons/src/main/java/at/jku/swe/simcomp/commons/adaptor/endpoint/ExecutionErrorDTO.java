package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents the error response of an execution.
 * It contains the current state of the robot.
 * Is returned when by the endpoints when a {@link at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException} is thrown.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionErrorDTO extends HttpErrorDTO{
    private RoboStateDTO state;
}