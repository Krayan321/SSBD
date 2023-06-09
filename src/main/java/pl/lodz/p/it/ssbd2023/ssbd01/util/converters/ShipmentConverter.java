package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShipmentConverter {
    private ShipmentConverter() {}

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

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
                .shipmentDate(shipment.getShipmentDate().toString())
                .shipmentMedications(
                        ShipmentMedicationConverter.mapShipmentMedsToShipmentMedsDto(
                                shipment.getShipmentMedications()))
                .build();
    }



    public static Shipment mapCreateShipmentDtoToShipment(CreateShipmentDTO shipment) {
        try {
            return Shipment.createBuilder()
                    .shipmentDate(Date.from(LocalDateTime.parse(shipment.getShipmentDate()).toInstant(ZoneOffset.UTC)))
                    .shipmentMedications(ShipmentMedicationConverter
                            .mapCreateShipmentMedsDtoToShipmentMeds(shipment.getShipmentMedications()))
                    .build();
        } catch(DateTimeParseException e) {
            throw ApplicationException.createIncorrectDateFormatException();
        }
    }


}
