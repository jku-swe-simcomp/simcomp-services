package at.jku.swe.simcomp.commons.registry.dto;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;

import java.util.Set;

public record ServiceRegistrationDisplayDTO(String name, Set<ActionType> supportedActions){}
