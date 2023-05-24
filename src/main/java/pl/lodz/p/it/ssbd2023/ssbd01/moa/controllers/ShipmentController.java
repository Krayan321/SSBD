package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;

@Path("shipment")
@RequestScoped
@DenyAll
public class ShipmentController extends AbstractController {

    @Inject private ShipmentFacade shipmentFacade;

    @GET
    @Path("/")
    public ShipmentDTO getShipment() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    //nie wiem co tutaj ma być
}
