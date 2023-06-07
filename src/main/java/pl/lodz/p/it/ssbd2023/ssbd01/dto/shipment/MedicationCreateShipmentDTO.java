package pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MedicationCreateShipmentDTO {
    @NotNull
    private long id;
}
