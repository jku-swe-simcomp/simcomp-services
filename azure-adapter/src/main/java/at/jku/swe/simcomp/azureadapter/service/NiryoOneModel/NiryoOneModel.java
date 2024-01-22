package at.jku.swe.simcomp.azureadapter.service.NiryoOneModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The NiryoOneModel class represents the joint angles of a Niryo One robotic arm.
 */
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

    /**
     * Constructs a NiryoOneModel instance with specified joint angles.
     *
     * @param joint1Angle Angle of joint 1.
     * @param joint2Angle Angle of joint 2.
     * @param joint3Angle Angle of joint 3.
     * @param joint4Angle Angle of joint 4.
     * @param joint5Angle Angle of joint 5.
     * @param joint6Angle Angle of joint 6.
     */
    public NiryoOneModel(double joint1Angle, double joint2Angle, double joint3Angle,
                         double joint4Angle, double joint5Angle, double joint6Angle) {
        this.joint1Angle = joint1Angle;
        this.joint2Angle = joint2Angle;
        this.joint3Angle = joint3Angle;
        this.joint4Angle = joint4Angle;
        this.joint5Angle = joint5Angle;
        this.joint6Angle = joint6Angle;
    }

    /**
     * Default constructor for NiryoOneModel.
     */
    public NiryoOneModel() {
        // Default constructor needed for Jackson deserialization.
    }

    /**
     * Gets the angle of joint 1.
     *
     * @return The angle of joint 1.
     */
    public double getJoint1Angle() {
        return joint1Angle;
    }

    /**
     * Sets the angle of joint 1.
     *
     * @param joint1Angle The angle of joint 1.
     */
    public void setJoint1Angle(double joint1Angle) {
        this.joint1Angle = joint1Angle;
    }

    /**
     * Gets the angle of joint 2.
     *
     * @return The angle of joint 2.
     */
    public double getJoint2Angle() {
        return joint2Angle;
    }

    /**
     * Sets the angle of joint 2.
     *
     * @param joint2Angle The angle of joint 2.
     */
    public void setJoint2Angle(double joint2Angle) {
        this.joint2Angle = joint2Angle;
    }

    /**
     * Gets the angle of joint 3.
     *
     * @return The angle of joint 3.
     */
    public double getJoint3Angle() {
        return joint3Angle;
    }

    /**
     * Sets the angle of joint 3.
     *
     * @param joint3Angle The angle of joint 3.
     */
    public void setJoint3Angle(double joint3Angle) {
        this.joint3Angle = joint3Angle;
    }

    /**
     * Gets the angle of joint 4.
     *
     * @return The angle of joint 4.
     */
    public double getJoint4Angle() {
        return joint4Angle;
    }

    /**
     * Sets the angle of joint 4.
     *
     * @param joint4Angle The angle of joint 4.
     */
    public void setJoint4Angle(double joint4Angle) {
        this.joint4Angle = joint4Angle;
    }

    /**
     * Gets the angle of joint 5.
     *
     * @return The angle of joint 5.
     */
    public double getJoint5Angle() {
        return joint5Angle;
    }

    /**
     * Sets the angle of joint 5.
     *
     * @param joint5Angle The angle of joint 5.
     */
    public void setJoint5Angle(double joint5Angle) {
        this.joint5Angle = joint5Angle;
    }

    /**
     * Gets the angle of joint 6.
     *
     * @return The angle of joint 6.
     */
    public double getJoint6Angle() {
        return joint6Angle;
    }

    /**
     * Sets the angle of joint 6.
     *
     * @param joint6Angle The angle of joint 6.
     */
    public void setJoint6Angle(double joint6Angle) {
        this.joint6Angle = joint6Angle;
    }
}