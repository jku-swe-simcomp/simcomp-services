package at.jku.swe.simcomp.commons.manager.dto.session;

import java.util.Map;
/**
 * This DTO is used to represent the state of a session and the simulations that are included in the session.
 */
public record SessionStateDTO(String sessionKey, SessionState sessionState, Map<String, SessionState> simulations) {}
