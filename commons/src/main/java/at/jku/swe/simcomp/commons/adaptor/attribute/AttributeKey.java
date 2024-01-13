package at.jku.swe.simcomp.commons.adaptor.attribute;

/**
 * Represents the possible keys for attributes.
 */
public enum AttributeKey {

    /**
     * The key for the attribute that represents the joint states.
     */
    JOINT_STATES,
    /**
     * The key for the attribute that represents the joint positions.
     */
    JOINT_POSITIONS,
    /**
     * The key for the attribute that represents position and orientation of the end-effector
     */
    POSE,
    /**
     * The key for the attribute that represents the end-effector orientation.
     */
    ORIENTATION,
    /**
     * The key for the attribute that represents the end-effector position.
     */
    POSITION
}
