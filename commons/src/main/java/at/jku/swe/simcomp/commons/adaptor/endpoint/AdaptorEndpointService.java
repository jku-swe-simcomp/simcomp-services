package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;

public interface AdaptorEndpointService {
    ExecutionResultDTO executeAction(ExecutionCommandDTO command);
    String getAttributeValue(String attributeName);
}
