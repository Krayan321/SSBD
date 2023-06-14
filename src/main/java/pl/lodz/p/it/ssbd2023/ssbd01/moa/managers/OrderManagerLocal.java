package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.List;

public interface OrderManagerLocal extends CommonManagerLocalInterface {

    Order createOrder(Account account, String localStorageData);

    Order getOrder(Long id);

    List<Order> getAllOrders();

    List<Order> getAllOrdersForSelf(Account account);

    List<Order> getWaitingOrders();

    List<Order> getOrdersToApprove();

    void approveOrder(Long id);

    void cancelOrder(Long id);

    void deleteWaitingOrderById(Long id);

    void withdrawOrder(Long id, Account account);

    void approvedByPatient(Long id, Account account);

    void addMedicationToOrder(Long id, OrderMedication orderMedication, Long version, Long medicationId);

    List<Medication> getOrderDetails(Long orderId);

    void updateQueue();

    void changeNumberOfMedicationsInOrder(Long orderId, Long medicationId, Integer quantity);
}
