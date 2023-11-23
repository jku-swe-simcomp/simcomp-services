package at.jku.swe.simcomp.commons.manager.dto.session;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SessionRequest.SelectedSimulationSessionRequest.class, name = "SELECTED"),
        @JsonSubTypes.Type(value = SessionRequest.AnySimulationSessionRequest.class, name = "ANY"),
        @JsonSubTypes.Type(value = SessionRequest.AnySimulationSessionRequest.class, name = "SELECTED_INSTANCE"),
})
public interface SessionRequest {
    Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException;
    record SelectedSimulationSessionRequest(List<String> requestedSimulations) implements SessionRequest{
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

    record SelectedSimulationInstanceSessionRequest(Map<String, String> requestedSimulationsInstances) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
            return visitor.initSession(this);
        }
    }
}
