package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;

import java.util.List;

public interface OrderManagerLocal extends CommonManagerLocalInterface {

    Order createOrder(Order order);

    Order getOrder(Long id);

    Order updateOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getAllOrdersForSelf(Account account);

    List<Order> getWaitingOrders(Account account);

    void aproveOrder(Long id);

    void cancelOrder(Long id);

    void addMedicationToOrder(Long id);

    Order getOrderDetails(Long id);

    void updateQueue();
}
