package at.jku.awe.simcomp.axisconverter.service;

import at.jku.swe.simcomp.axisconverter.service.InverseKinematics;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class InverseKinematicsTest {

    @Test
    void inverseKinematicsTest() {
        assertThrows(NullPointerException.class, () -> InverseKinematics.inverseKinematics(null));
    }
}
