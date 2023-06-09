package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;

import java.util.List;

public interface OrderManagerLocal extends CommonManagerLocalInterface {

    Order createOrder(Account Account, Long id);

    Order getOrder(Long id);

    Order updateOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getAllOrdersForSelf(Account account);

    List<Order> getWaitingOrders(Account account);

    void aproveOrder(Long id);

    void cancelOrder(Long id);

    void addMedicationToOrder(Long id, OrderMedication orderMedication, Long version, Long medicationId);

    List<Medication> getOrderDetails(Long orderId);

    void updateQueue();

    void changeNumberOfMedicationsInOrder(Long orderId, Long medicationId, Integer quantity);
}
