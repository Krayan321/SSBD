package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMedicationDTO extends AbstractEntityDTO {

    @Builder(builderMethodName = "orderMedicationBuilder")
    public OrderMedicationDTO(
            Long id,
            Long version,
            Order order,
            Medication medication,
            Integer quantity) {
        super(id, version);
        this.order = order;
        this.medication = medication;
        this.quantity = quantity;
    }

    public OrderMedication dtoToEntity() {
        OrderMedication orderMedication = new OrderMedication();
        orderMedication.setMedication(this.medication);
        orderMedication.setOrder(this.order);
        orderMedication.setQuantity(this.quantity);
        return orderMedication;
    }

    Order order;

    Medication medication;

    Integer quantity;

}

