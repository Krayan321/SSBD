package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

public class CreateOrderMedicationConverter {

    private CreateOrderMedicationConverter() {}

    public static OrderMedication mapCreateOrderMedicationDTOToOrderMedication(CreateOrderMedicationDTO createOrderMedicationDTO) {
        return OrderMedication.builder()
                .medication(Medication.builder().name(createOrderMedicationDTO.getName()).build())
                .quantity(createOrderMedicationDTO.getQuantity())
                .purchasePrice(createOrderMedicationDTO.getPurchasePrice())
                .build();
    }
}

