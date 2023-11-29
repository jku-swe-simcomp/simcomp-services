package at.jku.swe.simcomp.commons.manager.dto.session;


import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Information about an initialized session.")
public record SessionResponseDTO(String sessionKey, List<String> acquiredSimulations){}