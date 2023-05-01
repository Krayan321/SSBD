package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccessLevelConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;

@Path("account")
@RequestScoped
public class AccountController {

    @Inject
    private AccountManagerLocal accountManager;

    @POST
    public Response createClient() {
        return Response.status(Response.Status.CREATED).entity(null).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "DOCTOR", "CHEMIST", "PATIENT"})
    public Response readAllClients() {
        return Response.status(Response.Status.OK).entity("taniec").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readClient(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(id).build();
    }

    @POST
    @Path("/register")
    public Response register() {
        return Response.status(Response.Status.CREATED).entity(null).build();
    }

    @PUT
    @Path("/deactivate")
    public Response deactivate(@Valid AccountAndAccessLevelsDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/activate")
    public Response activate(@Valid AccountAndAccessLevelsDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/{id}/changeUserPassword")
    public Response changeUserPassword(@PathParam("id") Long id, @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(
            accountManager.updateUserPassword(id, newPassword)).build();

    }

    @PUT
    @Path("/changePassword")
    public Response changePassword(@Valid AccountAndAccessLevelsDTO accountDTO,
                                   @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/resetPassword")
    public Response resetPassword(@Valid AccountAndAccessLevelsDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/setNewPassword")
    public Response setNewPassword(@Valid AccountAndAccessLevelsDTO accountDTO,
                                   @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/{id}/grantPatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantPatient(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccountAndAccessLevels(accountManager.grantAccessLevel(id,
                        AccessLevelConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }

    @PUT
    @Path("/{id}/grantChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantChemist(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccountAndAccessLevels(accountManager.grantAccessLevel(id,
                        AccessLevelConverter.dtoToChemistData(chemistDataDTO)))
        ).build();
    }

    @PUT
    @Path("/{id}/grantAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantAdmin(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccountAndAccessLevels(accountManager.grantAccessLevel(id,
                        AccessLevelConverter.dtoToAdminData(adminDataDTO)))
        ).build();
    }

    @POST
    @Path("/patient")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createPatient(@Valid CreatePatientDto createPatientDto) {
        Account createdAccount = accountManager.createPatientAccount(
                AccountConverter.mapAccountDtoToAccount(createPatientDto.getAccountDTO(),
                        createPatientDto.getPassword()),
                AccessLevelConverter
                        .mapPatientDataDtoToPatientData(createPatientDto.getPatientDataDTO()));
        return Response.status(Response.Status.CREATED)
                .entity(createdAccount)
                .build();

    }

    @DELETE
    @Path("/{id}/removeRoleAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoleAdmin(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccount(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToAdminData(adminDataDTO)))
        ).build();
    }

    @DELETE
    @Path("/{id}/removeRoleChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoleChemist(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccount(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToChemistData(chemistDataDTO)))
        ).build();
    }

    @DELETE
    @Path("/{id}/removeRolePatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRolePatient(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccount(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }


}
