package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.OrderFacade;

import java.util.List;
import java.util.Objects;

public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {

    @Inject private OrderFacade orderFacade;

    @Override
    @DenyAll
    public Order createOrder(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Order getOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Order updateOrder(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Order> getAllOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Order> getAllOrdersForSelf() {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Order> getWaitingOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public void aproveOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public void cancelOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public void addMedicationToOrder(Long id, OrderMedication orderMedication, Long version) {
        Order order = getOrder(id);
        if (!Objects.equals(order.getVersion(), version)) {
            throw ApplicationException.createOptimisticLockException();
        }
        order.getOrderMedications().add(orderMedication);
        orderFacade.edit(order);
    }

    @Override
    @DenyAll
    public Order getOrderDetails(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public void updateQueue() {
        throw new UnsupportedOperationException();
    }
}
