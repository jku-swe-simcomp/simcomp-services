package at.jku.swe.simcomp.commons.manager.dto.session;


import java.util.List;

public record SessionResponseDTO(String sessionKey, List<String> acquiredSimulations){}