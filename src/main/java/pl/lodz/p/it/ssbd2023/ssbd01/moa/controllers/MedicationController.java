package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.MedicationManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.MedicationConverter;

@Path("medication")
@RequestScoped
@DenyAll
public class MedicationController extends AbstractController {

  @Inject private MedicationManagerLocal medicationManager;

  // moa 1
  @GET
  @Path("/")
  @DenyAll
  @Produces(MediaType.APPLICATION_JSON)
  public List<MedicationDTO> getAllMedications() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  // moa 2???
  @GET
  @Path("/{id}")
  @DenyAll
  @Produces(MediaType.APPLICATION_JSON)
  public MedicationDTO getMedication(@PathParam("id") Long id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  // chyba moa 19 ale może w shimpment
  @POST
  @Path("/add-medication")
  @DenyAll
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addMedication(MedicationDTO medicationDTO) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  // moa 20
  @PUT
  @Path("/{id}/edit-medication")
  public MedicationDTO editMedication(
      @Valid MedicationDTO medicationDTO, @PathParam("id") Long id) {
    var medication = MedicationConverter.mapMedicationDtoToMedication(medicationDTO);
    var version = medicationDTO.getVersion();
    var response =
        repeatTransaction(
            medicationManager,
            () ->
                medicationManager.editMedication(
                    id, version, medication, medicationDTO.getCategoryId()));
    return MedicationConverter.mapMedicationToMedicationDto(response);
  }

  // moa 2???
  @GET
  @Path("/{id}/details")
  @DenyAll
  @Produces(MediaType.APPLICATION_JSON)
  public MedicationDTO getMedicationDetails(@PathParam("id") Long id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
