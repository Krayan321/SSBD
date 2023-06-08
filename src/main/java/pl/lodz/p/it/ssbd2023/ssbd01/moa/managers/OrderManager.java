package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.OrderException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.OrderFacade;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {

    @Inject
    OrderFacade orderFacade;

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
    @RolesAllowed("getAllOrdersForSelf")
    public List<Order> getAllOrdersForSelf(Account account) {
        account
                .getAccessLevels()
                .forEach(
                        accessLevel -> {
                            if (!accessLevel.getRole().getRoleName().equals("PATIENT")) {
                                throw OrderException.onlyPatientCanListOrders();
                            }
                        });
        return orderFacade.findAllByPatientId(account.getId());
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
