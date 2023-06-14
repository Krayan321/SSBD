package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderPrescription;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

public class CreateOrderPrescriptionConverter {

    private CreateOrderPrescriptionConverter() {}

    public static Prescription mapCreateOrderPrescriptionDTOToPrescription(CreateOrderPrescription createOrderPrescriptionDTO) {
        return Prescription.builder()
                .prescriptionNumber(createOrderPrescriptionDTO.getPrescriptionNumber())
                .build();
    }
}

