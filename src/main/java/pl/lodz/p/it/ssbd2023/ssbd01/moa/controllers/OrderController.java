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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.OrderManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.OrderConverter;

import java.util.List;
import java.util.stream.Collectors;

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

    // moa 14
    @PUT
    @Path("/{id}/cancel")
    @DenyAll
    public void cancelOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 13
    @PUT
    @Path("/{id}/approve")
    @RolesAllowed("approveOrder")
    public Response approveOrder(@PathParam("id") Long id) {
        repeatTransactionVoid(orderManager, () -> orderManager.approveOrder(id));
        return Response.ok().build();
    }

    //moa 12
    @GET
    @Path("/to-approve")
    @RolesAllowed("getOrdersToApprove")
    public List<OrderDTO> getOrdersToApprove() {
        List<Order> orders = repeatTransaction(orderManager,
                () -> orderManager.getOrdersToApprove());
        return orders.stream()
                .map(OrderConverter::mapOrderToOrderDTO)
                .collect(Collectors.toList());
    }

    // moa 7
    @PUT
    @Path("/{id}/submit")
    @RolesAllowed("createOrder")
    public Response submitOrder(@PathParam("id") Long id, @Valid String patientDataId) {
        Order order = repeatTransaction(orderManager, () -> orderManager
//                .createOrder(accountManager.getCurrentUserWithAccessLevels(), id, patientDataId)); // todo
                .createOrder(accountManager.getCurrentUserWithAccessLevels(), id));

        OrderDTO orderDTO = OrderConverter.mapOrderToOrderDTO(order);

        return Response.ok(orderDTO).build();
    }

    // mok 9
    @GET
    @RolesAllowed("getWaitingOrders")
    @Path("/waiting")
    public List<OrderDTO> getWaitingOrders() {
        List<Order> orders = repeatTransaction(orderManager, () -> orderManager.getWaitingOrders());
                return orders.stream()
                        .map(OrderConverter::mapOrderToOrderDTO)
                        .collect(Collectors.toList());
    }

    //mok 8
   @DELETE
   @RolesAllowed("withdraw")
   @Path("/{id}/withdraw")
   public Response withdrawOrderById(@PathParam("id") Long id){
       repeatTransactionVoid(orderManager, () -> orderManager.withdrawOrder(id,accountManager.getCurrentUserWithAccessLevels()));
        return Response.ok().build();
   }


    // mok 10
    @PUT
    @RolesAllowed("deleteWaitingOrdersById")
    @Path("/{id}/waiting")
    public Response deleteWaitingOrderById(@PathParam("id") Long id) {
        repeatTransactionVoid(orderManager, () -> orderManager.deleteWaitingOrderById(id));
        return Response.ok().build();
    }

    //moa 16
    @PUT
    @Path("/update-queue")
    @RolesAllowed("updateQueue")
    public void updateQueue() {
        repeatTransactionVoidWithOptimisticLock(orderManager, () -> orderManager.updateQueue());
    }

    //moa 15
    @GET
    @Path("/statistics")
    @DenyAll
    public void showStatistics() {
        throw new UnsupportedOperationException();
    }

}
