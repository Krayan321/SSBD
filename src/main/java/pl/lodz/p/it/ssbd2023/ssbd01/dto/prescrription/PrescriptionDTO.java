package pl.lodz.p.it.ssbd2023.ssbd01.dto.prescrription;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDTO {

    @NotNull
    private String prescriptionNumber;

}
