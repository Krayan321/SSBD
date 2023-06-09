package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.ChangeMedNumberDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.OrderManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.OrderConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.OrderMedicationConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("order")
@RequestScoped
@DenyAll
@Log
public class OrderController extends AbstractController {

    @Inject
    private OrderManagerLocal orderManager;
    @Inject
    private AccountManagerLocal accountManager;


    //moa 18
    @GET
    @Path("/")
    @DenyAll
    public List<OrderDTO> getAllOrders() {
        throw new UnsupportedOperationException();
    }

    //moa 17
    @GET
    @RolesAllowed("getAllOrdersForSelf")
    @Path("/self")
    public Response getAllOrdersForSelf() {
        List<OrderDTO> orderDTOs = repeatTransaction(orderManager, () -> orderManager.getAllOrdersForSelf(accountManager.getCurrentUserWithAccessLevels()).stream()
                .map(OrderConverter::mapOrderToOrderDTO)
                .collect(Collectors.toList()));
        return Response.ok(orderDTOs).build();
    }

    // moa 14 - chyba tak
    @PUT
    @Path("/{id}/cancel")
    @DenyAll
    public void cancelOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 13
    @PUT
    @Path("/{id}/aprove")
    @DenyAll
    public void aproveOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 12
    @GET
    @Path("/approve")
    @DenyAll
    public List<OrderDTO> getOrdersToApprove() {
        throw new UnsupportedOperationException();
    }

    // moa3
    @PUT
    @Path("/{id}/add")
    @RolesAllowed("addMedicationToOrder")
    public Response addMedicationToOrder(@PathParam("id") Long id, @Valid CreateOrderMedicationDTO createOrderMedicationDTO) {
        repeatTransactionVoid(orderManager, () -> orderManager.addMedicationToOrder(id, OrderMedicationConverter.mapCreateOrderMedicationDTOToOrderMedication(createOrderMedicationDTO), createOrderMedicationDTO.getVersion(), createOrderMedicationDTO.getMedicationDTOId()));
        return Response.status(Response.Status.CREATED).build();
    }

    //moa 4
    @GET
    @Path("/{id}")
    @DenyAll
    public OrderDTO getOrderDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 5
    @PUT
    @Path("/{id}/change")
    @RolesAllowed("changeNumberOfMedicationsInOrder")
    public Response changeNumberOfMedicationsInOrder(@PathParam("id") Long id, @Valid ChangeMedNumberDTO changeMedNumberDTO) {
        repeatTransactionVoid(orderManager, () -> orderManager.changeNumberOfMedicationsInOrder(id, changeMedNumberDTO.getMedicationId(), changeMedNumberDTO.getQuantity()));
        return Response.ok().build();
    }

    //moa 6
    @PUT
    @Path("/{id}/remove")
    @DenyAll
    public void removeMedicationFromOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    // moa 7
    @PUT
    @Path("/{id}/submit")
    @RolesAllowed("createOrder")
    public Response submitOrder(@PathParam("id") Long id) {
        Order order = repeatTransaction(orderManager, () -> orderManager
                .createOrder(accountManager.getCurrentUserWithAccessLevels(), id));

        OrderDTO orderDTO = OrderConverter.mapOrderToOrderDTO(order);

        return Response.ok(orderDTO).build();
    }

    // moa 8
    @PUT
    @Path("/{id}/withdraw")
    @DenyAll
    public void withdrawOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    // mok 9
    @GET
    @RolesAllowed("getWaitingOrders")
    @Path("/waiting")
    public Response getWaitingOrders() {
        List<OrderDTO> orderDTOs = repeatTransaction(orderManager, () -> orderManager.getWaitingOrders(accountManager.getCurrentUserWithAccessLevels()).stream()
                .map(OrderConverter::mapOrderToOrderDTO)
                .collect(Collectors.toList()));
        return Response.ok(orderDTOs).build();
    }

    //moa 16
    @PUT
    @Path("/update-queue")
    @DenyAll
    public void updateQueue() {
        throw new UnsupportedOperationException();
    }

    //moa 15
    @GET
    @Path("/statistics")
    @DenyAll
    public void showStatistics() {
        throw new UnsupportedOperationException();
    }

}
