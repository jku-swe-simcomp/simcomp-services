package at.jku.swe.simcomp.commons.manager.dto.session;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SessionRequest.SelectedSimulationTypesSessionRequest.class, name = "SELECTED_TYPES"),
        @JsonSubTypes.Type(value = SessionRequest.AnySimulationSessionRequest.class, name = "ANY"),
        @JsonSubTypes.Type(value = SessionRequest.SelectedSimulationInstancesSessionRequest.class, name = "SELECTED_INSTANCES"),
})
@Schema(description = "Marker interface with subclasses to initialize a session.")
public interface SessionRequest {
    Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException;
    record SelectedSimulationTypesSessionRequest(List<String> requestedSimulationTypes) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
           return visitor.initSession(this);
        }
    }
    record AnySimulationSessionRequest(int n) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
            return visitor.initSession(this);
        }
    }

    record SelectedSimulationInstancesSessionRequest(Map<String, String> requestedSimulationInstances) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
            return visitor.initSession(this);
        }
    }
}
