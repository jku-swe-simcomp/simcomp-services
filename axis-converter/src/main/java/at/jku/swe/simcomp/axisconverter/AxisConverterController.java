package at.jku.swe.simcomp.axisconverter;


import at.jku.swe.simcomp.axisconverter.service.DirectKinematics;
import at.jku.swe.simcomp.axisconverter.service.InverseKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
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

    // TODO: change to other DTO
    @GetMapping("/axisToPos")
    public List<JointPositionDTO> convertAxesToPosition(PoseDTO position) {
        return InverseKinematics.inverseKinematics(position);
    }

    @GetMapping("/posToAxis")
    public PoseDTO convertPositionToAxes(List<JointPositionDTO> axes) {
        return DirectKinematics.directKinematics(axes);
    }
}
