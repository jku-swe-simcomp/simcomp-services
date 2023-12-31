package at.jku.swe.simcomp.commons.manager.dto;

import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectionDTO {
    @NonNull
    private boolean isAnomalyDetected;
    @NonNull
    private String description;
    @NonNull
    private List<RoboStateDTO> roboStates;
}
