package at.jku.swe.simcomp.commons.adaptor.execution.command;

import lombok.Getter;

@Getter
public enum ActionType {
    POSE("POSE"),
    SET_POSITION("SET_POSITION"),
    SET_ORIENTATION("SET_ORIENTATION"),
    GRAB("GRAB"),
    OPEN_HAND("OPEN_HAND"),
    ADJUST_JOINT_ANGLES("ADJUST_JOINT_ANGLES"),
    SET_JOINT_POSITIONS("SET_JOINT_POSITIONS"),
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
