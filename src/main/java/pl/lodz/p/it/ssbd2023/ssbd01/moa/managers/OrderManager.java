package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.SessionSynchronization;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;

import java.util.List;

public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {
    @Override
    public Order createOrder(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order getOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order updateOrder(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> getAllOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> getAllOrdersForSelf() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> getWaitingOrders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void aproveOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addMedicationToOrder(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order getOrderDetails(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateQueue() {
        throw new UnsupportedOperationException();
    }
}
