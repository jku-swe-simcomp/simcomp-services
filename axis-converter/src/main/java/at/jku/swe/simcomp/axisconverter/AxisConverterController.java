package at.jku.swe.simcomp.axisconverter;


import at.jku.swe.simcomp.axisconverter.service.DirectKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/converter")
@Slf4j
@Service
public class AxisConverterController {

    @GetMapping("/axisToPos")
    public List<JointAngleAdjustmentDTO> convertAxesToPosition(PoseDTO position) {
        return null;
    }

    @GetMapping("/posToAxis")
    public PoseDTO convertPositionToAxes(List<JointAngleAdjustmentDTO> axes) {
        return DirectKinematics.directKinematics(axes);
    }
}
