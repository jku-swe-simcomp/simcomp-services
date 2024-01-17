package at.jku.swe.simcomp.commons.manager.dto.session;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;

/**
 * This interface is used to implement the visitor pattern for session requests.
 */
public interface SessionRequestVisitor {
    Object initSession(SessionRequest.SelectedSimulationTypesSessionRequest request) throws SessionInitializationFailedException;
    Object initSession(SessionRequest.AnySimulationSessionRequest request) throws SessionInitializationFailedException;
    Object initSession(SessionRequest.SelectedSimulationInstancesSessionRequest request) throws SessionInitializationFailedException;
}
