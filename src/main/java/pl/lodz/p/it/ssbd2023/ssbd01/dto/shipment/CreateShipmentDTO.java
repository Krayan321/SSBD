package pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShipmentDTO {
    private String shipmentDate;
    @NotEmpty
    private List<CreateShipmentMedicationDTO> shipmentMedications;
}
