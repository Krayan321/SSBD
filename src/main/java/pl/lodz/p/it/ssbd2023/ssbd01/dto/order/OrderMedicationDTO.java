package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMedicationDTO {

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull
    private MedicationDTO medication;


}
