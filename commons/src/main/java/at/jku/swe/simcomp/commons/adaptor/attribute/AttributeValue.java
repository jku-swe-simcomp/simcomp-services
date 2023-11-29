package at.jku.swe.simcomp.commons.adaptor.attribute;

import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.OrientationDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJointStateDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "key")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttributeValue.JointPositions.class, name = "JOINT_POSITIONS"),
        @JsonSubTypes.Type(value = AttributeValue.JointStates.class, name = "JOINT_STATES"),
        @JsonSubTypes.Type(value = AttributeValue.Position.class, name = "POSITION"),
        @JsonSubTypes.Type(value = AttributeValue.Pose.class, name = "POSE"),
        @JsonSubTypes.Type(value = AttributeValue.Orientation.class, name = "ORIENTATION"),
})
public interface AttributeValue {
    record JointPositions(List<Double> jointPositions) implements AttributeValue{}
    record JointStates(List<RoboJointStateDTO> jointStates) implements AttributeValue{}

    record Pose(PoseDTO pose) implements AttributeValue{}
    record Position(PositionDTO position) implements AttributeValue{}
    record Orientation(OrientationDTO orientation) implements AttributeValue{}
}
