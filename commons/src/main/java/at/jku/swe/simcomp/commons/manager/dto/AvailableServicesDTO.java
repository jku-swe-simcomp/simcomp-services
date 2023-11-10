package at.jku.swe.simcomp.commons.manager.dto;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationDisplayDTO;

import java.util.List;

public record AvailableServicesDTO(List<ServiceRegistrationDisplayDTO> availableSimulations) {
}
