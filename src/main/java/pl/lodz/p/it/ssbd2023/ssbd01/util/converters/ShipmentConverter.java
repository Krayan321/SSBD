package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShipmentConverter {
    private ShipmentConverter() {}

    public static List<ShipmentDTO> mapShipmentsToShipmentsDto(List<Shipment> shipments) {
        return shipments == null ?
                null :
                shipments.stream()
                        .filter(Objects::nonNull)
                        .map(ShipmentConverter::mapShipmentToShipmentDto)
                        .collect(Collectors.toList());
    }

    public static ShipmentDTO mapShipmentToShipmentDto(Shipment shipment) {
        return ShipmentDTO.builder()
                .id(shipment.getId())
                .version(shipment.getVersion())
                .shipmentDate(shipment.getShipmentDate())
                .shipmentMedications(
                        ShipmentMedicationConverter.mapShipmentMedsToShipmentMedsDto(
                                shipment.getShipmentMedications()))
                .build();
    }



    public static Shipment mapCreateShipmentDtoToShipment(CreateShipmentDTO shipment) {
        return Shipment.createBuilder()
                .shipmentDate(shipment.getShipmentDate())
                .shipmentMedications(ShipmentMedicationConverter
                        .mapCreateShipmentMedsDtoToShipmentMeds(shipment.getShipmentMedications()))
                .build();
    }


}
