package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttributeService {
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;
    private final SessionRepository sessionRepository;

    public AttributeService(ServiceRegistryClient serviceRegistryClient,
                            AdaptorClient adaptorClient,
                            SessionRepository sessionRepository) {
        this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
        this.sessionRepository = sessionRepository;
    }

    public Map<String, AttributeValue> getAttributeValues(UUID sessionId, AttributeKey attributeKey){
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionId);
        List<AdaptorSession> sessions = session.getAdaptorSessions();
        List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs = serviceRegistryClient.getRegisteredAdaptors();
        var sessionToConfigs = sessions.stream()
                .collect(Collectors.toMap(Function.identity(), adaptorSession -> findMatchingConfig(adaptorSession, registeredAdaptorConfigs)));
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        for(var entry : sessionToConfigs.entrySet()){
            AdaptorSession adaptorSession = entry.getKey();
            if (entry.getValue().isEmpty()) {
                attributeValues.put(adaptorSession.getAdaptorName(), null);
                continue;
            }
            ServiceRegistrationConfigDTO config = entry.getValue().get();
            Optional<AttributeValue> value = adaptorClient.getAttributeValue(adaptorSession.getSessionKey(), attributeKey, config);
            attributeValues.put(adaptorSession.getAdaptorName(), value.orElse(null));
        }
        return attributeValues;
    }

    private Optional<ServiceRegistrationConfigDTO> findMatchingConfig(AdaptorSession session, List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs) {
        return registeredAdaptorConfigs.stream()
                .filter(config -> config.getName().equals(session.getAdaptorName()))
                .findFirst();
    }
}
