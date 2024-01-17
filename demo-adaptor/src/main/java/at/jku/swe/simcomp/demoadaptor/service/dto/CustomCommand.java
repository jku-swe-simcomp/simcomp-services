package at.jku.swe.simcomp.demoadaptor.service.dto;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "customType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CustomCommand.ChangeAltitude.class, name = "CHANGE_ALTITUDE")
})
public interface CustomCommand {
    record ChangeAltitude(double altitude) implements CustomCommand {}
}
