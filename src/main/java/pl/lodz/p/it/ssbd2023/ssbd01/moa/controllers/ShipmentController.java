package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.config.ETagFilterBinding;
import pl.lodz.p.it.ssbd2023.ssbd01.config.EntityIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.UpdateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.ShipmentManager;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.ShipmentManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.ShipmentConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.ShipmentMedicationConverter;

import java.util.List;

@Path("shipment")
@RequestScoped
@DenyAll
public class ShipmentController extends AbstractController {

    @Inject private ShipmentManagerLocal shipmentManager;

    @GET
    @Path("/")
    @RolesAllowed("readAllShipments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShipmentDTO> readAllShipments() {
        List<Shipment> shipments = repeatTransaction(shipmentManager,
                () -> shipmentManager.getAllShipments());
        return ShipmentConverter.mapShipmentsToShipmentsDto(shipments);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("readShipment")
    @Produces(MediaType.APPLICATION_JSON)
    public ShipmentDTO readShipment(@PathParam("id") Long id) {
        Shipment shipment = repeatTransaction(shipmentManager,
                () -> shipmentManager.getShipment(id));
        return ShipmentConverter.mapShipmentToShipmentDto(shipment);
    }

    @POST
    @Path("/")
    @RolesAllowed("createShipment")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createShipment(@Valid CreateShipmentDTO shipmentDTO) {
        Shipment shipment = ShipmentConverter
                .mapCreateShipmentDtoToShipment(shipmentDTO);
        repeatTransactionVoid(shipmentManager,
                () -> shipmentManager.createShipment(shipment));
        return Response.status(Response.Status.CREATED).build();
    }
}
