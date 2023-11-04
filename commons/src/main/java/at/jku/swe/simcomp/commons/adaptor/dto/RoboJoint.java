package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Getter;

@Getter
public enum RoboJoint {
    AXIS_1(1),
    AXIS_2(2),
    AXIS_3(3),
    AXIS_4(4),
    AXIS_5(5),
    AXIS_6(6);
    private final Integer index;
    private RoboJoint(Integer index) {
        this.index = index;
    }
}
