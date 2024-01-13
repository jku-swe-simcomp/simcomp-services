package at.jku.swe.simcomp.commons.manager.dto.session;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * This interface represents different types to request a session.
 */
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
    /**
     * This method is used to accept a visitor to initialize a session.
     * @param visitor The visitor
     * @return The result of the visitor
     * @throws SessionInitializationFailedException If the session initialization fails
     */
    Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException;

    /**
     * This class represents a session request for a specific set of simulation types.
     * @param requestedSimulationTypes The requested simulation types
     */
    record SelectedSimulationTypesSessionRequest(List<String> requestedSimulationTypes) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
           return visitor.initSession(this);
        }
    }

    /**
     * This class represents a session request for any simulation types.
     * @param n The number of simulations to be included in the session
     */
    record AnySimulationSessionRequest(int n) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
            return visitor.initSession(this);
        }
    }

    /**
     * This class represents a session request for a specific set of simulation instances.
     * @param requestedSimulationInstances The requested simulation instances
     */
    record SelectedSimulationInstancesSessionRequest(Map<String, String> requestedSimulationInstances) implements SessionRequest{
        @Override
        public Object accept(SessionRequestVisitor visitor) throws SessionInitializationFailedException {
            return visitor.initSession(this);
        }
    }
}
