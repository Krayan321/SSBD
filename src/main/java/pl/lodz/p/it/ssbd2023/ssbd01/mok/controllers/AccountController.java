package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
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

import java.util.ArrayList;
import java.util.List;

@Path("account")
@RequestScoped
@DenyAll
public class AccountController {

    @Inject
    private AccountManagerLocal accountManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public List<AccountDTO> readAllClients() {
        return new ArrayList<>();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
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
    @Path("/details")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PATIENT", "CHEMIST", "ADMIN"})
    public AccountAndAccessLevelsDTO readOwnAccount() {
        // read information about user own account
        return null;
    }

    @GET
    @Path("/{id}/details")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO readAccountAndAccessLevels(@PathParam("id") Long id) {
        Account account = accountManager.getAccountAndAccessLevels(id);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"GUEST"})
    public Response registerPatientAccount(@NotNull @Valid RegisterPatientDto registerPatientDto) {
        Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}/deactivate")
    @RolesAllowed({"ADMIN"})
    public AccountDTO deactivateAccount(@PathParam("id") Long id) {
        // todo
        return null;
    }

    @PUT
    @Path("{id}/activate")
    @RolesAllowed({"ADMIN"})
    public AccountDTO activateAccount(@PathParam("id") Long id) {
        Account account = accountManager.activateUserAccount(id);
        return AccountConverter.mapAccountToAccountDto(account);
    }

    @PUT
    @Path("/{id}/changeUserPassword")
    @RolesAllowed({"ADMIN"})
    public AccountDTO changeUserPassword(@PathParam("id") Long id, @Valid UpdateOtherUserPasswordDTO updateOtherUserPasswordDTO) {
        String newPassword = updateOtherUserPasswordDTO.getPassword();
        Account account = accountManager.updateUserPassword(id, newPassword);
        return AccountConverter.mapAccountToAccountDto(account);
    }

    @PUT
    @Path("/changePassword")
    @RolesAllowed({"PATIENT", "CHEMIST", "ADMIN"})
    public Response changePassword(@Valid ChangePasswordDTO changePasswordDTO) {
        // todo
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/resetPassword")
    @RolesAllowed({"GUEST"})
    public Response resetPassword(@Valid ResetPasswordDTO resetPasswordDTO) {
        String email = resetPasswordDTO.getEmail();
        // todo
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("/setNewPassword")
    @RolesAllowed({"GUEST"})
    public Response setNewPassword(@Valid SetNewPasswordDTO setNewPasswordDTO) {
        // todo
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/{id}/patient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        PatientData patientData = AccessLevelConverter.mapPatientDataDtoToPatientData(patientDataDTO);
        Account account = accountManager.editAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/chemist")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        ChemistData chemistData = AccessLevelConverter.mapChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.editAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.editAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantPatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantPatient(@PathParam("id") Long id, @Valid CreatePatientDataDTO patientDataDTO) {
        PatientData patientData = AccessLevelConverter.mapCreatePatientDataDTOtoPatientData(patientDataDTO);
        Account account = accountManager.grantAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantChemist(@PathParam("id") Long id, @Valid CreateChemistDataDTO chemistDataDTO) {
        ChemistData chemistData = AccessLevelConverter.mapCreateChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.grantAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantAdmin(@PathParam("id") Long id, @Valid CreateAdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapCreateAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.grantAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @DELETE
    @Path("/{id}/removeRoleAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
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
    @RolesAllowed({"ADMIN"})
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
    @RolesAllowed({"ADMIN"})
    public Response removeRolePatient(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.mapAccountToAccountDto(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }


}
