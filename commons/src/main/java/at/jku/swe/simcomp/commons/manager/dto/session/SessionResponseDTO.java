package at.jku.swe.simcomp.commons.manager.dto.session;


import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * This DTO is used to represent a session response.
 * @param sessionKey The session key
 * @param acquiredSimulations The acquired simulations
 */
@Schema(description = "Information about an initialized session.")
public record SessionResponseDTO(String sessionKey, List<String> acquiredSimulations){}