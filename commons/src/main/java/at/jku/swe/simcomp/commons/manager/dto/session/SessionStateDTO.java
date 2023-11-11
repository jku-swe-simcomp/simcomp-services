package at.jku.swe.simcomp.commons.manager.dto.session;

import java.util.Map;

public record SessionStateDTO(String sessionKey, SessionState sessionState, Map<String, SessionState> simulations) {}
