package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.prescrription.PrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.PrescriptionManagerLocal;

import java.util.List;

@Path("prescription")
@RequestScoped
@DenyAll
public class PrescriptionController extends AbstractController {

    @Inject private PrescriptionManagerLocal prescriptionManagerLocal;

    @GET
    @Path("/")
    public List<PrescriptionDTO> readAllPrescriptions() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GET
    @Path("/{id}")
    public Response readPrescription(@PathParam("id") Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @POST
    @Path("/create-prescription")
    public Response createPrescription(PrescriptionDTO prescriptionDTO) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PUT
    @Path("/update-prescription")
    public PrescriptionDTO updatePrescription(PrescriptionDTO prescriptionDTO) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
