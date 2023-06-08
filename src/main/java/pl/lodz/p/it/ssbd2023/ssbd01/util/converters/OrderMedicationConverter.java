package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

public class OrderMedicationConverter {

    private OrderMedicationConverter() {}

    public static OrderMedication mapCreateOrderMedicationDTOToOrderMedication(CreateOrderMedicationDTO createOrderMedicationDTO) {
        return OrderMedication.builder()
                .quantity(createOrderMedicationDTO.getQuantity())
                .build();
    }
}

