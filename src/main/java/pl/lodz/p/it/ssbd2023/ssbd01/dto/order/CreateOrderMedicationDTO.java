package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderMedicationDTO extends AbstractEntityDTO {

    @Builder(builderMethodName = "orderMedicationBuilder")
    public CreateOrderMedicationDTO(
            Long id,
            Long version,
            Long medicationDTOId,
            Integer quantity) {
        super(id, version);
        this.medicationDTOId = medicationDTOId;
        this.quantity = quantity;
    }
    @NotNull
    Long medicationDTOId;
    @NotNull
    Integer quantity;

}

