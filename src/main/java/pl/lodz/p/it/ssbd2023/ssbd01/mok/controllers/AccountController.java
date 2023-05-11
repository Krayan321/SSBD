package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountAndAccessLevelsDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChangePasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.SetNewPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.UpdateOtherUserPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.VerificationTokenDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccessLevelConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;

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

    @POST
    @Path("/confirm")
    public Response confirmAccount(@Valid VerificationTokenDto token) {
        accountManager.confirmAccountRegistration(token.getToken());
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountDTO readAccount(@PathParam("id") Long id) {
        Account account = accountManager.getAccount(id);
        return AccountConverter.mapAccountToAccountDto(account);
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
//    @RolesAllowed({"GUEST"})
    public Response registerPatientAccount(@NotNull @Valid RegisterPatientDTO registerPatientDto) {
        Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}/deactivate")
    @RolesAllowed({"ADMIN"})
    public AccountDTO deactivateAccount(@PathParam("id") Long id) {
        Account account = accountManager.deactivateUserAccount(id);
        return AccountConverter.mapAccountToAccountDto(account);
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
    public AccountDTO changeUserPassword(@PathParam("id") Long id,
                                         @Valid UpdateOtherUserPasswordDTO updateOtherUserPasswordDTO) {
        String newPassword = updateOtherUserPasswordDTO.getPassword();
        Account account = accountManager.updateUserPassword(id, newPassword);
        return AccountConverter.mapAccountToAccountDto(account);
    }

    @PUT
    @Path("{id}/changePassword")
    @RolesAllowed({"PATIENT", "CHEMIST", "ADMIN"})
    public Response changePassword(@PathParam("id") Long id, @Valid ChangePasswordDTO changePasswordDTO) {
        String oldPassword = changePasswordDTO.getOldPassword();
        String newPassword = changePasswordDTO.getNewPassword();
        accountManager.updateOwnPassword(id, oldPassword, newPassword);
        return Response.status(Response.Status.OK).build();
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
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id,
                                                     @Valid EditPatientDataDTO patientDataDTO) {
        PatientData patientData =
                AccessLevelConverter.mapEditPatientDataDtoToPatientData(patientDataDTO);
        Account account = accountManager.editAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/chemist")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO editChemistData(@PathParam("id") Long id,
                                                     @Valid EditChemistDataDTO chemistDataDTO) {
        ChemistData chemistData =
                AccessLevelConverter.mapEditChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.editAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO editPatientData(@PathParam("id") Long id,
                                                     @Valid EditAdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapEditAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.editAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantPatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantPatient(@PathParam("id") Long id,
                                                  @Valid GrantPatientDataDTO patientDataDTO) {
        PatientData patientData =
                AccessLevelConverter.mapGrantPatientDataDTOtoPatientData(patientDataDTO);
        Account account = accountManager.grantAccessLevel(id, patientData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantChemist(@PathParam("id") Long id,
                                                  @Valid GrantChemistDataDTO chemistDataDTO) {
        ChemistData chemistData =
                AccessLevelConverter.mapGrantChemistDataDtoToChemistData(chemistDataDTO);
        Account account = accountManager.grantAccessLevel(id, chemistData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @PUT
    @Path("/{id}/grantAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public AccountAndAccessLevelsDTO grantAdmin(@PathParam("id") Long id,
                                                @Valid GrantAdminDataDTO adminDataDTO) {
        AdminData adminData = AccessLevelConverter.mapGrantAdminDataDtoToAdminData(adminDataDTO);
        Account account = accountManager.grantAccessLevel(id, adminData);
        return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    }

    @POST
    @Path("/add-patient")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response addPatientAccountAsAdmin(
            @NotNull @Valid AddPatientAccountDto addPatientAccountDto) {
        Account account = AccountConverter.mapAddPatientDtoToAccount(addPatientAccountDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/add-chemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response addChemistAccountAsAdmin(
            @NotNull @Valid AddChemistAccountDto addChemistAccountDto) {
        Account account = AccountConverter.mapChemistDtoToAccount(addChemistAccountDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/add-admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response addAdminAccountAsAdmin(@NotNull @Valid AddAdminAccountDto addAdminAccountDto) {
        Account account = AccountConverter.mapAdminDtoToAccount(addAdminAccountDto);
        accountManager.registerAccount(account);
        return Response.status(Response.Status.CREATED).build();
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
    public Response removeRoleChemist(@PathParam("id") Long id,
                                      @Valid ChemistDataDTO chemistDataDTO) {
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
    public Response removeRolePatient(@PathParam("id") Long id,
                                      @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.mapAccountToAccountDto(accountManager.removeAccessLevel(id,
                        AccessLevelConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }


}
