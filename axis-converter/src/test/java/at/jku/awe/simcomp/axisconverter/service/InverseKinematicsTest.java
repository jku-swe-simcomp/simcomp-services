package at.jku.awe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.axisconverter.service.InverseKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.OrientationDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class InverseKinematicsTest {

    @Test
    void inverseKinematicsTest() {
        assertThrows(NullPointerException.class, () -> InverseKinematics.inverseKinematics(null));

        PoseDTO position = new PoseDTO(
                new PositionDTO(100.0, 100.0, 100.0),
                new OrientationDTO(100.0, 100.0, 100.0)
        );

        List<JointPositionDTO> jointPositions = InverseKinematics.inverseKinematics(position);
        assertEquals(jointPositions.get(0).getRadians(), 0.8, 0.01);
        assertEquals(jointPositions.get(1).getRadians(), 0.68571, 0.01);
        assertEquals(jointPositions.get(2).getRadians(), 0.9, 0.01);
        assertEquals(jointPositions.get(3).getRadians(), 0.0, 0.01);
        assertEquals(jointPositions.get(4).getRadians(), 0.42857, 0.01);
        assertEquals(jointPositions.get(5).getRadians(), 0.0, 0.01);

        jointPositions = InverseKinematics.inverseKinematics(position, 10);
        assertEquals(jointPositions.get(0).getRadians(), 0.93333, 0.01);
        assertEquals(jointPositions.get(1).getRadians(), 0.8, 0.01);
        assertEquals(jointPositions.get(2).getRadians(), 0.988888, 0.01);
        assertEquals(jointPositions.get(3).getRadians(), -2.0, 0.01);
        assertEquals(jointPositions.get(4).getRadians(), 1.16666, 0.01);
        assertEquals(jointPositions.get(5).getRadians(), 0.0, 0.01);
    }
}
