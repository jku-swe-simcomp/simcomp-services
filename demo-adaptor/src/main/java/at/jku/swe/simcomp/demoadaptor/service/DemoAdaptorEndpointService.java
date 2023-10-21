package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService;
import org.springframework.stereotype.Service;

@Service
public class DemoAdaptorEndpointService implements AdaptorEndpointService {
    @Override
    public ExecutionResultDTO executeAction(ExecutionCommandDTO command) {
        return ExecutionResultDTO.builder()
                .message("Executed action")
                .success(true)
                .build();
    }

    @Override
    public String getAttributeValue(String attributeName) {
        return "42";
    }
}
