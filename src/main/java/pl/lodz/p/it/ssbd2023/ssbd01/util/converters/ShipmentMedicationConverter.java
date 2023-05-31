package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShipmentMedicationConverter {

    public static List<ShipmentMedicationDTO> mapShipmentMedsToShipmentMedsDto(
            List<ShipmentMedication> shipmentMedications) {
        return shipmentMedications == null ?
                null :
                shipmentMedications.stream()
                        .filter(Objects::nonNull)
                        .map(ShipmentMedicationConverter::mapShipmentMedToShipmentMedDto)
                        .collect(Collectors.toList());
    }

    public static ShipmentMedicationDTO mapShipmentMedToShipmentMedDto(
            ShipmentMedication shipmentMedication) {
        return ShipmentMedicationDTO.builder()
                .quantity(shipmentMedication.getQuantity())
                .medication(MedicationConverter
                        .mapMedicationToMedicationDto(shipmentMedication.getMedication()))
                .build();
    }

    public static List<ShipmentMedication> mapShipmentMedsDtoToShipmentMeds(
            List<ShipmentMedicationDTO> shipmentMedicationDTOs) {
        return shipmentMedicationDTOs == null ?
                null :
                shipmentMedicationDTOs.stream()
                        .filter(Objects::nonNull)
                        .map(ShipmentMedicationConverter::mapShipmentMedDtoToShipmentMed)
                        .collect(Collectors.toList());
    }

    public static ShipmentMedication mapShipmentMedDtoToShipmentMed(
            ShipmentMedicationDTO shipmentMedicationDTO) {
        return ShipmentMedication.builder()
                .medication(MedicationConverter.
                        mapMedicationDtoToMedication(
                                shipmentMedicationDTO.getMedication()))
                .quantity(shipmentMedicationDTO.getQuantity())
                .build();
    }
}
