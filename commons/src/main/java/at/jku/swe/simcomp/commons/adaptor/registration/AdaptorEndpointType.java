package at.jku.swe.simcomp.commons.adaptor.registration;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint;

/**
 * Types of endpoints.
 */
public enum AdaptorEndpointType {
    /**
     * Endpoint type for executing an action.
     * Endpoints with this type have to accept POST request and implement
     * the {@link at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint#executeAction(ExecutionCommandDTO)}
     * method.
     */
    EXECUTE_ACTION,
    /**
     * Endpoint type for getting an attribute.
     * Endpoints with this type have to accept get requests and implement
     * the {@link AbstractAdaptorEndpoint#getAttribute()}
     * method.
     */
    GET_ATTRIBUTE,
    /**
     * Endpoint type for health checks.
     * Endpoints with this type have to accept get requests and implement
     * the {@link AbstractAdaptorEndpoint#healthCheck()}
     * method.
     */
    HEALTH_CHECK
}
