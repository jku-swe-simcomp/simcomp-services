package at.jku.swe.simcomp.commons.manager.dto;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationDisplayDTO;

import java.util.List;

/**
 * This DTO is used to represent adaptors for users
 * @param availableSimulations a list of available simulations
 */
public record AvailableServicesDTO(List<ServiceRegistrationDisplayDTO> availableSimulations) {
}
