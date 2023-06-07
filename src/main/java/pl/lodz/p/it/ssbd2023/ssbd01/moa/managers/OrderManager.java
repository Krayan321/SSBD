package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.OrderException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.OrderFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;

import java.util.List;
import java.util.Objects;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {

    @Inject
    OrderFacade orderFacade;
    @Inject
    MedicationFacade medicationFacade;
    @Override
    @RolesAllowed("createOrder")
    public Order createOrder(Account account, Long id) {
        Order order = getOrder(id);
        account
                .getAccessLevels()
                .forEach(
                        accessLevel -> {
                            if (!accessLevel.getRole().getRoleName().equals("PATIENT")) {
                                throw OrderException.onlyPatientCanOrder();
                            }
                        });

        /*if(!Objects.equals(order.getVersion(), version)){
            throw ApplicationException.createOptimisticLockException();
        }*/

        // Sprawdzenie, czy w bazie są wszystkie leki potrzebne do realizacji zamówienia
        boolean allMedicationsAvailable = checkAllMedicationsAvailable(order);
        order.setInQueue(!allMedicationsAvailable);
        //nie ma pola do zatwierdzenia
        /*if (order.getPrescription() != null) {
            order.setInQueue(false); // Zamówienie wymaga zatwierdzenia przez aptekarza
        }*/



        orderFacade.create(order);
        if(!order.getInQueue())
        decreaseMedicationStock(order);

        return order;
    }

    private void decreaseMedicationStock(Order order) {
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            int requestedQuantity = orderMedication.getQuantity();

            int currentStock = medication.getStock();
            int updatedStock = currentStock - requestedQuantity;
            medication.setStock(updatedStock);
            medicationFacade.edit(medication);
        }
    }

    private boolean checkAllMedicationsAvailable(Order order) {
        // Sprawdzenie, czy wszystkie leki w zamówieniu są dostępne na stanie

        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            int requestedQuantity = orderMedication.getQuantity();

            if (medication.getStock() < requestedQuantity) {
                return false;
            }
        }

        return true;
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
