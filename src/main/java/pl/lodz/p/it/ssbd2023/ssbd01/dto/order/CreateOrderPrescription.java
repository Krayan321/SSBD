package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderPrescription {
    @NotNull
    private String prescriptionNumber;
}
