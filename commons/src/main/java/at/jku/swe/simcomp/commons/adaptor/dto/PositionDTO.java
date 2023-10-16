package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PositionDTO {
    @NonNull
    private double x;
    @NonNull
    private double y;
    @NonNull
    private double z;
}
