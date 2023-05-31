package pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;

import java.util.Date;
import java.util.List;

@Data()
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO extends AbstractEntityDTO {

    private Date shipmentDate;

    private List<ShipmentMedicationDTO> shipmentMedications;

    @Builder
    public ShipmentDTO(Long id, Long version, Date shipmentDate,
                       List<ShipmentMedicationDTO> shipmentMedications) {
        super(id, version);
        this.shipmentDate = shipmentDate;
        this.shipmentMedications = shipmentMedications;
    }
}
