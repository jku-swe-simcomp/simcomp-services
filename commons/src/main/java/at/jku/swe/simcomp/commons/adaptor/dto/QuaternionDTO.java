package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuaternionDTO {
    @NonNull
    private double x;
    @NonNull
    private double y;
    @NonNull
    private double z;
    @NonNull
    private double w;
}
