package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

import java.util.List;
import java.util.stream.Collectors;

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
    Order order = new Order();
    order.setOrderDate(createOrderDTO.getOrderDate());

    // Konwersja listy OrderMedicationDTO na listę OrderMedication
    List<OrderMedication> orderMedications = createOrderDTO.getOrderMedication().stream()
            .map(CreateOrderMedicationConverter::mapCreateOrderMedicationDTOToOrderMedication)
            .collect(Collectors.toList());
    order.setOrderMedications(orderMedications);

    // Konwersja CreateOrderPrescription na Prescription
    Prescription prescription = CreateOrderPrescriptionConverter.mapCreateOrderPrescriptionDTOToPrescription(createOrderDTO.getPrescription());
    order.setPrescription(prescription);

    return order;
  }



}
