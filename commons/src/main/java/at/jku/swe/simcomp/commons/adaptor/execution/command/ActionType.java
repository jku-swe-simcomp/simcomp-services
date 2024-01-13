package at.jku.swe.simcomp.commons.adaptor.execution.command;

import lombok.Getter;

/**
 * This enum represents the different action types.
 */
@Getter
public enum ActionType {
    POSE("POSE"),
    SET_POSITION("SET_POSITION"),
    SET_ORIENTATION("SET_ORIENTATION"),
    GRAB("GRAB"),
    OPEN_HAND("OPEN_HAND"),
    ADJUST_JOINT_ANGLE("ADJUST_JOINT_ANGLE"),
    SET_JOINT_POSITION("SET_JOINT_POSITION"),
    SET_SPEED("SET_SPEED"),
    PAUSE("PAUSE"),
    RESUME("RESUME"),
    RESET_TO_HOME("RESET_TO_HOME"),
    STOP("STOP"),
    CALIBRATE("CALIBRATE"),
    TOGGLE_GRIPPER_MODE("TOGGLE_GRIPPER_MODE"),
    COMPOSITE("COMPOSITE");

    private final String actionName;
    ActionType(String name){
       this.actionName = name;
    }

}
