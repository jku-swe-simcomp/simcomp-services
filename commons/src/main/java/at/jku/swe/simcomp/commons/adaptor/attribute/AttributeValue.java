package at.jku.swe.simcomp.commons.adaptor.attribute;

import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.QuaternionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJointStateDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

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
    @Schema(description = "The key of the attribute",
            example = "JOINT_POSITIONS")
    record JointPositions(@NonNull List<Double> jointPositions) implements AttributeValue{}
    record JointStates(@NonNull List<RoboJointStateDTO> jointStates) implements AttributeValue{}

    record Pose(@NonNull PoseDTO pose) implements AttributeValue{}
    record Position(@NonNull PositionDTO position) implements AttributeValue{}
    record Orientation(@NonNull QuaternionDTO orientation) implements AttributeValue{}
}
