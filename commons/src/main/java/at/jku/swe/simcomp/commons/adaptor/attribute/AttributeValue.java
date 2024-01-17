package at.jku.swe.simcomp.commons.adaptor.attribute;

import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.OrientationDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJointStateDTO;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.util.List;

/**
 * Represents the possible values for attributes.
 */
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
@Schema(description = "Marker interface with different subclasses representing attribute values.",
        example = "{ \"key\": \"JOINT_POSITIONS\", \"jointPositions\": [0.0,0.0,0.0,0.0,0.0,0.0]")
public interface AttributeValue {
    /**
     * Represents the joint positions.
     * @param jointPositions The joint positions.
     */
    @Schema(description = "An array with 6 entries describing the positions of the joints in radians.",example = "{\"jointPositions\": [0.0,0.0,0.0,0.0,0.0,0.0]")
    record JointPositions(@NonNull List<Double> jointPositions) implements AttributeValue{}
    /**
     * Represents the joint states.
     * @param jointStates The joint states.
     */
    @Schema(description = "An array with 6 entries describing the state of the joints.")
    record JointStates(@NonNull List<RoboJointStateDTO> jointStates) implements AttributeValue{}

    /**
     * Represents the position and orientation of the end-effector.
     * @param pose The position and orientation of the end-effector.
     */
    @Schema(description = "The position and orientation of the end-effector.")
    record Pose(@NonNull PoseDTO pose) implements AttributeValue{}

    /**
     * Represents the position of the end-effector.
     * @param position The position of the end-effector.
     */
    @Schema(description = "The position of the end-effector.")
    record Position(@NonNull PositionDTO position) implements AttributeValue{}

    /**
     * Represents the orientation of the end-effector.
     * @param orientation The orientation of the end-effector.
     */
    @Schema(description = "The orientation of the end-effector as quaternion.")
    record Orientation(@NonNull OrientationDTO orientation) implements AttributeValue{}
}
