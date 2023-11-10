package at.jku.swe.simcomp.commons.manager.dto.session;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.ToString;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SessionRequest.SelectedSimulationSessionRequest.class, name = "SELECTED"),
        @JsonSubTypes.Type(value = SessionRequest.AnySimulationSessionRequest.class, name = "ANY"),
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
}
