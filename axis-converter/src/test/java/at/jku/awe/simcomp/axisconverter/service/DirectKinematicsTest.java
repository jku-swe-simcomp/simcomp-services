package at.jku.awe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.axisconverter.service.DirectKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DirectKinematicsTest {

    @Test
    void directKinematicsTest() {
        List<JointPositionDTO> input = new LinkedList<>();
        input.add(new JointPositionDTO(RoboJoint.AXIS_1, 0.0));
        input.add(new JointPositionDTO(RoboJoint.AXIS_2, 0.0));
        input.add(new JointPositionDTO(RoboJoint.AXIS_3, 0.0));
        input.add(new JointPositionDTO(RoboJoint.AXIS_4, 0.0));
        input.add(new JointPositionDTO(RoboJoint.AXIS_5, 0.0));
        input.add(new JointPositionDTO(RoboJoint.AXIS_6, 0.0));
        PoseDTO position1 = DirectKinematics.directKinematics(input);

        assertEquals(245.2, position1.getPosition().getX(), 0.01);
        assertEquals(0.0, position1.getPosition().getY(), 0.01);
        assertEquals(417.5, position1.getPosition().getZ(), 0.01);
        assertEquals(0.0, position1.getOrientation().getX(), 0.01);
        assertEquals(0.0, position1.getOrientation().getY(), 0.01);
        assertEquals(0.0, position1.getOrientation().getZ(), 0.01);

        input.remove(1);

        assertThrows(IllegalArgumentException.class, () -> DirectKinematics.directKinematics(input));

        List<JointPositionDTO> input2 = new LinkedList<>();
        input2.add(new JointPositionDTO(RoboJoint.AXIS_1, 2.0));
        input2.add(new JointPositionDTO(RoboJoint.AXIS_2, -1.0));
        input2.add(new JointPositionDTO(RoboJoint.AXIS_3, 0.6));
        input2.add(new JointPositionDTO(RoboJoint.AXIS_4, 1.0));
        input2.add(new JointPositionDTO(RoboJoint.AXIS_5, -0.4));
        input2.add(new JointPositionDTO(RoboJoint.AXIS_6, 0.5));
        PoseDTO position2 = DirectKinematics.directKinematics(input2);

        assertEquals(-12.139535599316549, position2.getPosition().getX(), 0.01);
        assertEquals(34.943942955282246, position2.getPosition().getY(), 0.01);
        assertEquals(421.7581145227432, position2.getPosition().getZ(), 0.01);
        assertEquals(0.1109503908511381, position2.getOrientation().getX(), 0.01);
        assertEquals(1.6733803756951708, position2.getOrientation().getY(), 0.01);
        assertEquals(-0.9819615778213698, position2.getOrientation().getZ(), 0.01);

    }
}
