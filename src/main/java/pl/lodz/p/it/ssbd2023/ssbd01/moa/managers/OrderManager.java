package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
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
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentMedicationFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@Stateful
@DenyAll
public class OrderManager extends AbstractManager
        implements OrderManagerLocal, SessionSynchronization {

    @Inject
    private OrderFacade orderFacade;
    @Inject
    private MedicationFacade medicationFacade;
    @Inject
    private ShipmentFacade shipmentFacade;
    @Inject
    private AccountFacade accountFacade;
    @Context
    private SecurityContext context;
    @Inject
    private ShipmentMedicationFacade shipmentMedicationFacade;


    @Override
    @RolesAllowed("createOrder")
    public void createOrder(Order order) {
        Account account = getCurrentUserWithAccessLevels();
        order.setPatientData(AccessLevelFinder.findPatientData(account));

        order.getOrderMedications().forEach(om -> {
            om.setOrder(order);
            Medication medication = medicationFacade.findByName(
                    om.getMedication().getName());
            om.setMedication(medication);
            om.setPurchasePrice(medication.getCurrentPrice());
        });

        if (!checkAllMedicationsAvailable(order)) {
            order.setOrderState(OrderState.IN_QUEUE);
        } else {
            decreaseMedicationStock(order, null);
            if(checkIsOnPrescription(order)) {
                if(order.getPrescription() == null) {
                    throw OrderException.createPrescriptionRequired();
                }
                order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);
            } else {
                order.setOrderState(OrderState.FINALISED);
            }
        }
        orderFacade.create(order);
    }

    @RolesAllowed("createOrder")
    private boolean checkAllMedicationsAvailable(Order order) {
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            if (medication.getStock() <= orderMedication.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    @RolesAllowed("createOrder")
    private boolean checkIsOnPrescription(Order order) {
        for (OrderMedication om : order.getOrderMedications()) {
            if (om.getMedication().getCategory().getIsOnPrescription()) {
                return true;
            }
        }
        return false;
    }

    @RolesAllowed("createOrder")
    private void decreaseMedicationStock(Order order, EtagVerification etagVerification) {
        for (OrderMedication orderMedication : order.getOrderMedications()) {
            Medication medication = orderMedication.getMedication();
            int requestedQuantity = orderMedication.getQuantity();

            int currentStock = medication.getStock();
            int updatedStock = currentStock - requestedQuantity;
            medication.setStock(updatedStock);
            medicationFacade.edit(medication);
        }
    }

    @Override
    @RolesAllowed("updateQueue")
    public void updateQueue() {
        List<Order> ordersInQueue = orderFacade.findAllOrdersInQueueSortByOrderDate();
        List<Shipment> shipmentsNotProcessed = shipmentFacade.findAllNotAlreadyProcessed();

        shipmentsNotProcessed.forEach(
                shipment -> {
                    shipment
                            .getShipmentMedications()
                            .forEach(
                                    shipmentMedication -> {
                                        shipmentMedication
                                                .getMedication()
                                                .setStock(
                                                        shipmentMedication.getMedication().getStock()
                                                                + shipmentMedication.getQuantity());
                                    });
                    shipment.setWasAlreadyProcessed(true);
                });

        AtomicBoolean canAllMedicationsBeProceed = new AtomicBoolean(true);
        AtomicBoolean sendForPatientAproval = new AtomicBoolean(false);
        ordersInQueue.forEach(
                order -> {
                    for (OrderMedication orderMedication : order.getOrderMedications()) {

                        Medication medication = orderMedication.getMedication();

                        if (orderMedication.getPurchasePrice() != null && (medication.getCurrentPrice().compareTo(orderMedication.getPurchasePrice()) > 0)) {
                            sendForPatientAproval.getAndSet(true);
                        }

                        if (medication.getStock() < orderMedication.getQuantity()) {
                            canAllMedicationsBeProceed.getAndSet(false);
                            break;
                        }
                    }

                    if (canAllMedicationsBeProceed.get()) {

                        if (sendForPatientAproval.get()) {
                            order.setOrderState(OrderState.TO_BE_APPROVED_BY_PATIENT);
                        }

                        if (!sendForPatientAproval.get() && order.getPrescription() == null) {
                            for (OrderMedication orderMedication : order.getOrderMedications()) {
                                Medication medication = orderMedication.getMedication();
                                medication.setStock(medication.getStock() - orderMedication.getQuantity());
                            }
                            order.setOrderState(OrderState.FINALISED);
                        }
                    }
                });
    }
    @PermitAll
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
    @RolesAllowed("approveOrder")
    public void approveOrder(Long id) {
        Order order = orderFacade.find(id).orElseThrow();
        if (!order.getOrderState().equals(OrderState.WAITING_FOR_CHEMIST_APPROVAL)) {
            throw OrderException.createModificationOrderOfIllegalState();
        }
        order.setOrderState(OrderState.FINALISED);
        orderFacade.edit(order);
    }

    @Override
    @RolesAllowed("cancelOrder")
    public void cancelOrder(Long id) {
        Account account = getCurrentUser();
        Order order = orderFacade.find(id).orElseThrow();
        if (!order.getOrderState().equals(OrderState.WAITING_FOR_CHEMIST_APPROVAL)) {
            throw OrderException.createModificationOrderOfIllegalState();
        }
        orderFacade.cancelOrder(id, account.getId());
    }

    @Override
    @RolesAllowed("withdraw")
    public void withdrawOrder(Long id) {
        Account account = getCurrentUser();
        Optional<Order> order = orderFacade.find(id);
        if (order.get().getOrderState() != OrderState.TO_BE_APPROVED_BY_PATIENT
                || (account.getId() != order.get().getPatientData().getId())) {
            throw OrderException.noPermissionToDeleteOrder();
        }
        orderFacade.withdrawOrder(id, account.getId());

    }

    @Override
    @RolesAllowed("approvedByPatient")
    public void approvedByPatient(Long id) {
        Account account = getCurrentUser();
        Order order = orderFacade.find(id)
                .orElseThrow(() -> OrderException.orderNotFound(id));

        if (order.getOrderState() != OrderState.TO_BE_APPROVED_BY_PATIENT
                || (account.getId() != order.getPatientData().getId())) {
            throw OrderException.noPermissionToApproveOrder();
        }

        orderFacade.approvedByPatient(id, account.getId());
        decreaseMedicationStock(order);

        if (order.getPrescription() != null) {
            order.setOrderState(OrderState.WAITING_FOR_CHEMIST_APPROVAL);
        } else {
            order.setOrderState(OrderState.FINALISED);
        }
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
    @RolesAllowed("addMedicationToOrder")
    public void addMedicationToOrder(
            Long id, OrderMedication orderMedication, Long version, Long medicationId) {
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

        order
                .get()
                .getOrderMedications()
                .forEach(
                        orderMedication -> {
                            res.add(orderMedication.getMedication());
                        });
        return res;
    }

    @RolesAllowed("getCurrentUser")
    public Account getCurrentUser() {
        return accountFacade.findByLogin(getCurrentUserLogin());
    }

    @RolesAllowed("getCurrentUserWithAccessLevels")
    public Account getCurrentUserWithAccessLevels() {

        return accountFacade.findByLoginAndRefresh(getCurrentUserLogin());
    }


    @PermitAll
    public String getCurrentUserLogin() {
        return context.getUserPrincipal().getName();
    }
}
