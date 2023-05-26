package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;

import java.util.List;

public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {
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
    public void addMedicationToOrder(Long id) {
        throw new UnsupportedOperationException();
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
