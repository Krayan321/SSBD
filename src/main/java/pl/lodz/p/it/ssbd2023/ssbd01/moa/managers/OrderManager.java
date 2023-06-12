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
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.OrderException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.OrderFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@Stateful
@DenyAll
public class OrderManager extends AbstractManager implements OrderManagerLocal, SessionSynchronization {

    @Inject
    private OrderFacade orderFacade;
    @Inject
    private MedicationFacade medicationFacade;
    @Inject
    private ShipmentFacade shipmentFacade;

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

        order.setOrderState(OrderState.CREATED);
        // Sprawdzenie, czy w bazie są wszystkie leki potrzebne do realizacji zamówienia
        boolean allMedicationsAvailable = checkAllMedicationsAvailable(order);

        if (!allMedicationsAvailable) {
            order.setOrderState(OrderState.IN_QUEUE);
        }

        if (order.getOrderState() != OrderState.IN_QUEUE) {
            decreaseMedicationStock(order);
            if (order.getPrescription() != null) {
                order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);// Zamówienie wymaga zatwierdzenia przez aptekarza
            }
        }

        orderFacade.create(order);

        return order;
    }

    @Override
    @PermitAll
    public void updateOrderQueue() {
        List<Order> ordersInQueue = orderFacade.findAllOrdersInQueueSortByOrderDate();
        List<Shipment> shipmentsNotProcessed = shipmentFacade.findAllNotAlreadyProcessed();

        shipmentsNotProcessed.forEach(
                shipment -> {

                    shipment.getShipmentMedications().forEach(
                            shipmentMedication -> {
                                shipmentMedication.getMedication().setStock(shipmentMedication.getMedication().getStock() + shipmentMedication.getQuantity());
                            }
                    );
                    shipment.setWasAlreadyProcessed(true);
                }
        );


        AtomicBoolean canAllMedicationsBeProceed = new AtomicBoolean(true);
        AtomicBoolean sendForPatientAproval = new AtomicBoolean(false);
        ordersInQueue.forEach(
                order -> {
                    for (OrderMedication orderMedication : order.getOrderMedications()) {
                        Medication medication = orderMedication.getMedication();

                        if (medication.getCurrentPrice().compareTo(medication.getPreviousPrice()) > 0) {
                            sendForPatientAproval.getAndSet(true);
                        }

                        if (medication.getStock() < orderMedication.getQuantity()) {
                            canAllMedicationsBeProceed.getAndSet(false);
                            break;
                        }
                    }

                    if (canAllMedicationsBeProceed.get()) {

                        if (sendForPatientAproval.get()) {
                            //todo send for patient aproval
                        }

                        if (order.getPrescription() != null) {
                            //todo send for chemist aproval
                        }

                        if (!sendForPatientAproval.get() && order.getPrescription() == null) {
                            for (OrderMedication orderMedication : order.getOrderMedications()) {
                                Medication medication = orderMedication.getMedication();
                                medication.setStock(medication.getStock() - orderMedication.getQuantity());
                            }
                            order.setOrderState(OrderState.APPROVED);
                        }

                    }

                }

        );


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
    @RolesAllowed("getWaitingOrders")
    public List<Order> getWaitingOrders() {
        return orderFacade.findWaitingOrders();
    }

    @Override
    @RolesAllowed("getOrdersToApprove")
    public List<Order> getOrdersToApprove() {
        return orderFacade.findNotYetApproved();
    }

    @Override
    @RolesAllowed("deleteWaitingOrdersById")
    public void deleteWaitingOrderById(Long id) {
        Optional<Order> order = orderFacade.find(id);
        if (order.get().getOrderState() != OrderState.IN_QUEUE) {
            throw OrderException.orderNotInQueue();
        }
        orderFacade.deleteWaitingOrdersById(id);

    }

    @Override
    public void cancelOrder(Long id, Account account) {

    }

    @Override
    @DenyAll
    public void approveOrder(Long id) {
        throw new UnsupportedOperationException();
    }

//    @Override todo
//    @RolesAllowed("withdraw")
//    public void cancelOrder(Long id, Account account) {
//        Optional<Order> order = orderFacade.find(id);
//        if(!order.get().getInQueue() || order.get().getPatientApproved()
//                || (account.getId() != order.get().getPatientData().getId())){
//            throw OrderException.noPermissionToDeleteOrder();
//        }
//        orderFacade.withdrawOrder(id, account.getId());
//
//    }

    @Override
    @RolesAllowed("addMedicationToOrder")
    public void addMedicationToOrder(Long id, OrderMedication orderMedication, Long version, Long medicationId) {
        Optional<Order> optOrder = orderFacade.find(id);
        Optional<Medication> optMedication = medicationFacade.find(medicationId);
        if (optOrder.isEmpty()) {
            throw ApplicationException.createEntityNotFoundException();
        }
        if (optMedication.isEmpty()) {
            throw ApplicationException.createEntityNotFoundException();
        }
        if (!(optOrder.get().getVersion().equals(version))) {
            throw ApplicationException.createOptimisticLockException();
        }
        orderMedication.setMedication(optMedication.get());
        orderMedication.setOrder(optOrder.get());
        optOrder.get().getOrderMedications().add(orderMedication);
        orderFacade.edit(optOrder.get());
    }

    @Override
    @PermitAll
    public List<Medication> getOrderDetails(Long orderId) {
        Optional<Order> order = orderFacade.find(orderId);
        List<Medication> res = new ArrayList<>();

        if (order.isEmpty()) {
            throw OrderException.createEntityNotFoundException();
        }

        order.get()
                .getOrderMedications()
                .forEach(
                        orderMedication -> {
                            res.add(orderMedication.getMedication());
                        }
                );
        return res;
    }

    @Override
    @DenyAll
    public void updateQueue() {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("changeNumberOfMedicationsInOrder")
    public void changeNumberOfMedicationsInOrder(Long orderId, Long medicationId, Integer quantity) {
        Optional<Order> optOrder = orderFacade.find(orderId);

        if (optOrder.isEmpty()) {
            throw OrderException.createEntityNotFoundException();
        }
        optOrder.get()
                .getOrderMedications()
                .forEach(
                        orderMedication -> {
                            if (Objects.equals(orderMedication.getMedication().getId(), medicationId)) {
                                orderMedication.setQuantity(quantity);
                            }
                        }
                );
        orderFacade.edit(optOrder.get());
    }
}
