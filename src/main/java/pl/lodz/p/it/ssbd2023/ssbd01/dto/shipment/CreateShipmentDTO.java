package pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShipmentDTO {
    private Date shipmentDate;
    private List<ShipmentMedicationDTO> shipmentMedications;
}
