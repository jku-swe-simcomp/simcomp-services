package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoseDTO {
    @NonNull
    private PositionDTO position;
    @NonNull
    private OrientationDTO orientation;
}
