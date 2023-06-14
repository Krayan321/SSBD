package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotNull
    private Date orderDate;

    @NotNull
    private List<OrderMedicationDTO> orderMedication;

    @NotNull
    private CreateOrderPrescription prescription;
}
