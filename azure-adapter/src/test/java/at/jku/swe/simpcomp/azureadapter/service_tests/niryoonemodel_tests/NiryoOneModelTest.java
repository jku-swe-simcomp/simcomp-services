package at.jku.swe.simpcomp.azureadapter.service_tests.niryoonemodel_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneModel.NiryoOneModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NiryoOneModelTest {

    @Test
    public void testGetterAndSetter() {
        NiryoOneModel niryoOneModel = new NiryoOneModel();

        niryoOneModel.setJoint1Angle(45.0);
        niryoOneModel.setJoint2Angle(90.0);
        niryoOneModel.setJoint3Angle(135.0);
        niryoOneModel.setJoint4Angle(180.0);
        niryoOneModel.setJoint5Angle(225.0);
        niryoOneModel.setJoint6Angle(270.0);

        assertEquals(45.0, niryoOneModel.getJoint1Angle());
        assertEquals(90.0, niryoOneModel.getJoint2Angle());
        assertEquals(135.0, niryoOneModel.getJoint3Angle());
        assertEquals(180.0, niryoOneModel.getJoint4Angle());
        assertEquals(225.0, niryoOneModel.getJoint5Angle());
        assertEquals(270.0, niryoOneModel.getJoint6Angle());
    }

    @Test
    public void testDefaultConstructor() {
        NiryoOneModel niryoOneModel = new NiryoOneModel();

        // Verify that all angles are initialized to 0.0
        assertEquals(0.0, niryoOneModel.getJoint1Angle());
        assertEquals(0.0, niryoOneModel.getJoint2Angle());
        assertEquals(0.0, niryoOneModel.getJoint3Angle());
        assertEquals(0.0, niryoOneModel.getJoint4Angle());
        assertEquals(0.0, niryoOneModel.getJoint5Angle());
        assertEquals(0.0, niryoOneModel.getJoint6Angle());
    }

    @Test
    public void testParameterizedConstructor() {
        NiryoOneModel niryoOneModel = new NiryoOneModel(45.0, 90.0, 135.0, 180.0, 225.0, 270.0);

        // Verify that the constructor initializes angles correctly
        assertEquals(45.0, niryoOneModel.getJoint1Angle());
        assertEquals(90.0, niryoOneModel.getJoint2Angle());
        assertEquals(135.0, niryoOneModel.getJoint3Angle());
        assertEquals(180.0, niryoOneModel.getJoint4Angle());
        assertEquals(225.0, niryoOneModel.getJoint5Angle());
        assertEquals(270.0, niryoOneModel.getJoint6Angle());
    }
}
