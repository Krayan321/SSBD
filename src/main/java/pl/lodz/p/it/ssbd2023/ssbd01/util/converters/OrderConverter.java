package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderPrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class OrderConverter {

  private OrderConverter() {}

  public static OrderDTO mapOrderToOrderDTO(Order order) {
    return OrderDTO.builder()
        .orderMedication(
            order.getOrderMedications().stream()
                .map(OrderMedicationConverter::mapOrderMedicationToOrderMedicationDTO)
                .toList())
        .patientData(AccessLevelConverter
                .mapPatientDataToPatientDataDto(order.getPatientData()))
        .prescription(
            order.getPrescription() == null
                ? null
                : PrescriptionConverter.mapPrescriptionToPrescriptionDTO(order.getPrescription()))
        .orderDate(order.getOrderDate())
        .orderState(order.getOrderState())
        .id(order.getId())
        .build();
  }

  public static Order mapCreateOrderDTOToOrder(CreateOrderDTO createOrderDTO) {
    return Order.createBuilder()
            .orderDate(Date.from(LocalDateTime.parse(createOrderDTO.getOrderDate())
                    .toInstant(ZoneOffset.UTC)))
            .prescription(mapCreateOrderPrescriptionToPrescription(
                    createOrderDTO.getPrescription()))
            .orderMedications(OrderMedicationConverter.mapCreateOrderMedicationsDTOToOrderMedications(
                    createOrderDTO.getOrderMedications()))
            .build();
  }

  public static Prescription mapCreateOrderPrescriptionToPrescription(
          CreateOrderPrescriptionDTO prescription) {
    return new Prescription(prescription.getPrescriptionNumber());
  }
}




