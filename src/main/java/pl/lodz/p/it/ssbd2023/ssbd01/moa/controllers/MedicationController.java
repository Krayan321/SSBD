package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.AddMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.CategoryManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.MedicationManagerLocal;

import java.util.List;

@Path("medication")
@RequestScoped
@DenyAll
@Log
public class MedicationController extends AbstractController {

    @Inject
    private MedicationManagerLocal medicationManager;

    @Inject
    private CategoryManagerLocal categoryManager;

    //moa 1
    @GET
    @Path("/")
    @DenyAll
    @Produces(MediaType.APPLICATION_JSON)
    public List<AddMedicationDTO> getAllMedications() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //moa 2???
    @GET
    @Path("/{id}")
    @DenyAll
    @Produces(MediaType.APPLICATION_JSON)
    public AddMedicationDTO getMedication(@PathParam("id") Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //chyba moa 19 ale może w shimpment
    @POST
    @Path("/add-medication")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMedication(@Valid AddMedicationDTO addMedicationDTO) {
        Category category = categoryManager.findByName(addMedicationDTO.getCategoryName());
        Medication medication =
                Medication.builder()
                        .name(addMedicationDTO.getName())
                        .stock(addMedicationDTO.getStock())
                        .price(addMedicationDTO.getPrice())
                        .category(category)
                        .build();

        repeatTransaction(medicationManager, () -> medicationManager.createMedication(medication));
        return Response.status(Response.Status.CREATED).build();
    }

    //moa 20
    @PUT
    @Path("/{id}/edit-medication")
    @DenyAll
    public AddMedicationDTO editMedication(AddMedicationDTO addMedicationDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //moa 2???
    @GET
    @Path("/{id}/details")
    @DenyAll
    @Produces(MediaType.APPLICATION_JSON)
    public AddMedicationDTO getMedicationDetails(@PathParam("id") Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}