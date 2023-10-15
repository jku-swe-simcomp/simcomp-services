package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AnomalyDetectionDTO {
    private boolean isAnomalyDetected;
    private String description;
    private List<RoboStateDTO> roboStates;
}
