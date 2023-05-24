package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.OrderDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.OrderFacade;

import java.util.List;

@Path("order")
@RequestScoped
@DenyAll
public class OrderController extends AbstractController {

    @Inject private OrderFacade orderFacade;

    //moa 18
    @GET
    @Path("/")
    public List<OrderDTO> getAllOrders() {
        throw new UnsupportedOperationException();
    }

    //moa 17
    @GET
    @Path("/self")
    public List<OrderDTO> getAllOrdersForSelf() {
        throw new UnsupportedOperationException();
    }

    // moa 14 - chyba tak
    @PUT
    @Path("/{id}/cancel")
    public void cancelOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 13
    @PUT
    @Path("/{id}/aprove")
    public void aproveOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 12
    @GET
    @Path("/waiting")
    public List<OrderDTO> getWaitingOrders() {
        throw new UnsupportedOperationException();
    }

    // moa3
    @PUT
    @Path("/{id}/add")
    public void addMedicationToOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 4
    @GET
    @Path("/{id}")
    public OrderDTO getOrderDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 5
    @PUT
    @Path("/{id}/change")
    public void changeNumberOfMedicationsInOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 6
    @PUT
    @Path("/{id}/remove")
    public void removeMedicationFromOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    // moa 7
    @PUT
    @Path("/{id}/submit")
    public void submitOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    // moa 8
    @PUT
    @Path("/{id}/withdraw")
    public void withdrawOrder(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 16
    @PUT
    @Path("/update-queue")
    public void updateQueue() {
        throw new UnsupportedOperationException();
    }

    //moa 15
    @GET
    @Path("/statistics")
    public void showStatistics() {
        throw new UnsupportedOperationException();
    }

}
