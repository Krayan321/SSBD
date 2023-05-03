package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
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
    @RolesAllowed({"ADMIN", "CHEMIST", "PATIENT"})
    public Response readAllClients() {
        return Response.status(Response.Status.OK).entity("taniec").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDTO readAccount(@PathParam("id") Long id) {
        Account account = accountManager.getAccount(id);
        return AccountConverter.mapAccountToAccountDto(account);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(@PathParam("id") Long id, @Valid AccountDTO accountDTO) {
        Account account = null;
        return Response.status(Response.Status.OK).entity(AccountConverter.mapAccountToAccountDto(accountManager.updateAccount(id, account))).build();
    }

    @GET
    @Path("/{id}/details")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountAndAccessLevelsDTO readAccountAndAccessLevels(@PathParam("id") Long id) {
        Account account = accountManager.getAccountAndAccessLevels(id);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerPatientAccount(@NotNull @Valid RegisterPatientDto registerPatientDto) {
        Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
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
    @Path("{id}/activateAccount")
    public Response activateAccount(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(AccountConverter.mapAccountToAccountDto(accountManager.activateUserAccount(id))).build();
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
    @Path("/{id}/patient")
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        PatientData patientData = AccessLevelConverter.mapPatientDataDtoToPatientData(patientDataDTO);
        Account account = accountManager.editAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/chemist")
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        ChemistData chemistData = AccessLevelConverter.mapChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.editAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/admin")
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.editAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantPatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountAndAccessLevelsDTO grantPatient(@PathParam("id") Long id, @Valid CreatePatientDataDTO patientDataDTO) {
        PatientData patientData = AccessLevelConverter.mapCreatePatientDataDTOtoPatientData(patientDataDTO);
        Account account = accountManager.grantAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountAndAccessLevelsDTO grantChemist(@PathParam("id") Long id, @Valid CreateChemistDataDTO chemistDataDTO) {
        ChemistData chemistData = AccessLevelConverter.mapCreateChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.grantAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AccountAndAccessLevelsDTO grantAdmin(@PathParam("id") Long id, @Valid CreateAdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapCreateAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.grantAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @DELETE
    @Path("/{id}/removeRoleAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoleAdmin(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.mapAccountToAccountDto(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToAdminData(adminDataDTO)))
        ).build();
    }

    @DELETE
    @Path("/{id}/removeRoleChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoleChemist(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.mapAccountToAccountDto(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.mapChemistDataDtoToChemistData(chemistDataDTO)))
        ).build();
    }

    @DELETE
    @Path("/{id}/removeRolePatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRolePatient(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.mapAccountToAccountDto(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }


}
