package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuaternionDTO {
    @NonNull
    private Double x;
    @NonNull
    private Double y;
    @NonNull
    private Double z;
    @NonNull
    private Double w;
}
