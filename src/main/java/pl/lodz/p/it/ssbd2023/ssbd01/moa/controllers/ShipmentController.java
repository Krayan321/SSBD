package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.ShipmentManagerLocal;

import java.util.List;

@Path("shipment")
@RequestScoped
@DenyAll
public class ShipmentController extends AbstractController {

    @Inject private ShipmentManagerLocal shipmentManagerLocal;

    @GET
    @Path("/")
    public List<ShipmentDTO> readAllShipments() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/{id}")
    public Response readShipment(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/create-shipment")
    public Response createShipment(ShipmentDTO shipmentDTO) {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path("/update-shipment")
    public ShipmentDTO updateShipment(ShipmentDTO shipmentDTO) {
        throw new UnsupportedOperationException();
    }
}
