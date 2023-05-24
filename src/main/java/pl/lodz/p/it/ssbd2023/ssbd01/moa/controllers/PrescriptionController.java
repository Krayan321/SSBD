package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.PrescriptionManagerLocal;

@Path("prescription")
@RequestScoped
@DenyAll
public class PrescriptionController extends AbstractController {

    @Inject private PrescriptionManagerLocal prescriptionManagerLocal;

    @Path("/")
    public void getPrescription() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    //nie wiem co tutaj ma być
}
