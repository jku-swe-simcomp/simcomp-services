package at.jku.swe.simcomp.azureadapter.service.NiryoOneModel;

import at.jku.swe.simcomp.azureadapter.service.HelperClasses.DeserializerService;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NiryoOneModel {

    @JsonProperty("joint1_angle")
    private double joint1Angle;

    @JsonProperty("joint2_angle")
    private double joint2Angle;

    @JsonProperty("joint3_angle")
    private double joint3Angle;

    @JsonProperty("joint4_angle")
    private double joint4Angle;

    @JsonProperty("joint5_angle")
    private double joint5Angle;

    @JsonProperty("joint6_angle")
    private double joint6Angle;

    private DeserializerService deserializerService;

    public NiryoOneModel(double joint1Angle, double joint2Angle, double joint3Angle, double joint4Angle, double joint5Angle, double joint6Angle, DeserializerService deserializerService) {
        this.joint1Angle = joint1Angle;
        this.joint2Angle = joint2Angle;
        this.joint3Angle = joint3Angle;
        this.joint4Angle = joint4Angle;
        this.joint5Angle = joint5Angle;
        this.joint6Angle = joint6Angle;
        this.deserializerService = deserializerService;
    }

    public double getJoint1Angle() {
        return joint1Angle;
    }

    public void setJoint1Angle(double joint1Angle) {
        this.joint1Angle = joint1Angle;
    }

    public double getJoint2Angle() {
        return joint2Angle;
    }

    public void setJoint2Angle(double joint2Angle) {
        this.joint2Angle = joint2Angle;
    }

    public double getJoint3Angle() {
        return joint3Angle;
    }

    public void setJoint3Angle(double joint3Angle) {
        this.joint3Angle = joint3Angle;
    }

    public double getJoint4Angle() {
        return joint4Angle;
    }

    public void setJoint4Angle(double joint4Angle) {
        this.joint4Angle = joint4Angle;
    }

    public double getJoint5Angle() {
        return joint5Angle;
    }

    public void setJoint5Angle(double joint5Angle) {
        this.joint5Angle = joint5Angle;
    }

    public double getJoint6Angle() {
        return joint6Angle;
    }

    public void setJoint6Angle(double joint6Angle) {
        this.joint6Angle = joint6Angle;
    }

}
