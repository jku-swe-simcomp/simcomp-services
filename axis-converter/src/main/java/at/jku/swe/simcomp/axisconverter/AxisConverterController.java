package at.jku.swe.simcomp.axisconverter;


import at.jku.swe.simcomp.axisconverter.service.DirectKinematics;
import at.jku.swe.simcomp.axisconverter.service.InverseKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/converter")
@Slf4j
public class AxisConverterController {

    @PostMapping("/poseToAxis")
    public List<JointPositionDTO> convertAxesToPosition(@RequestBody PoseDTO position,
                                                        @RequestParam(required = false, defaultValue = "15") Integer granularity) {
        System.out.println(granularity);
        return InverseKinematics.inverseKinematics(position, granularity);
    }

    @PostMapping("/axisToPose")
    public PoseDTO convertPositionToAxes(@RequestBody List<JointPositionDTO> axes) {
        return DirectKinematics.directKinematics(axes);
    }
}
