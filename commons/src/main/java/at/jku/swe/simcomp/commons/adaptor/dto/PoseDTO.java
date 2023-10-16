package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoseDTO {
    @NonNull
    private PositionDTO position;
    @NonNull
    private QuaternionDTO quaternion;
}
