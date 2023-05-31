package pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ShipmentMedicationDTO {
    private Integer quantity;
    private MedicationDTO medication;
}
