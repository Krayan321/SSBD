package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.NewPasswordDTO;
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
public class AccountController extends AbstractController {

  @Inject private AccountManagerLocal accountManager;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public List<AccountDTO> readAllClients() {
    List<Account> accounts = accountManager.getAllAccounts();
//            repeatTransaction(accountManager, () -> accountManager.getAccount(id));
return accounts.stream().map(AccountConverter::mapAccountToAccountDto).toList();
  }

  @POST
  @Path("/confirm")
  public Response confirmAccount(@Valid VerificationTokenDto token) {
    accountManager.confirmAccountRegistration(token.getToken());
    //    repeatTransactionVoid(
    //        accountManager, () -> accountManager.confirmAccountRegistration(token.getToken()));
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountDTO readAccount(@PathParam("id") Long id) {
    Account account = accountManager.getAccount(id);
    //            repeatTransaction(accountManager, () -> accountManager.getAccount(id));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @GET
  @Path("/details")
  @Produces(MediaType.APPLICATION_JSON)
  public SelfAccountWithAccessLevelDTO readOwnAccount() {
    Account account = accountManager.getCurrentUser();
//            repeatTransaction(accountManager, () -> accountManager.getCurrentUser());
    return  AccountConverter.mapAccountToSelfAccountWithAccessLevelsDto(account);
  }

  @GET
  @Path("/{id}/details")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO readAccountAndAccessLevels(@PathParam("id") Long id) {
    Account account = accountManager.getAccountAndAccessLevels(id);
    //        repeatTransaction(accountManager, () -> accountManager.getAccountAndAccessLevels(id));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerPatientAccount(@NotNull @Valid RegisterPatientDTO registerPatientDto) {
    Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
    accountManager.registerAccount(account);
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Path("/{id}/block")
  public Response blockAccount(@PathParam("id") Long id) {
    accountManager.blockAccount(id);
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{id}/unblock")
  public Response unblockAccount(@PathParam("id") Long id) {
    accountManager.unblockAccount(id);
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("{id}/activate")
  public AccountDTO activateAccount(@PathParam("id") Long id) {
    Account account = accountManager.activateUserAccount(id);
    //        repeatTransaction(accountManager, () -> accountManager.activateUserAccount(id));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/{id}/changeUserPassword")
  public AccountDTO changeUserPassword(
      @PathParam("id") Long id, @Valid UpdateOtherUserPasswordDTO updateOtherUserPasswordDTO) {
    String newPassword = updateOtherUserPasswordDTO.getPassword();
    Account account = accountManager.updateUserPassword(id, newPassword);
    //        repeatTransaction(accountManager, () -> accountManager.updateUserPassword(id,
    // newPassword));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("{id}/changePassword")
  public Response changePassword(
      @PathParam("id") Long id, @Valid ChangePasswordDTO changePasswordDTO) {
    // todo get login from security context
    String oldPassword = changePasswordDTO.getOldPassword();
    String newPassword = changePasswordDTO.getNewPassword();
    accountManager.updateOwnPassword(id, oldPassword, newPassword); //todo
    //    repeatTransaction(
    //        accountManager, () -> accountManager.updateOwnPassword(id, oldPassword, newPassword));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("{id}/editAccount")
  public Response editAccount(@PathParam("id") Long id, @Valid EditAccountDTO editAccountDTO) {
    String email = editAccountDTO.getEmail();
    accountManager.updateOwnEmail(id, email);
    //    repeatTransaction(
    //        accountManager, () -> accountManager.updateOwnEmail(id, email));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/reset-password")
  public Response resetPassword(@Valid ResetPasswordDTO resetPasswordDTO) {
    accountManager.sendResetPasswordToken(resetPasswordDTO.getEmail());
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/new-password")
  public Response setNewPassword(@Valid NewPasswordDTO newPasswordDTO) {
    accountManager.setNewPassword(newPasswordDTO.getToken(), newPasswordDTO.getPassword());
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{id}/patient")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO editPatientData(
      @PathParam("id") Long id, @Valid EditPatientDataDTO patientDataDTO) {
    PatientData patientData =
        AccessLevelConverter.mapEditPatientDataDtoToPatientData(patientDataDTO);
    Account account = accountManager.editAccessLevel(id, patientData, patientDataDTO.getVersion());
//        repeatTransaction(accountManager, () -> accountManager.editAccessLevel(id, patientData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/admin")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO editAdminData(
      @PathParam("id") Long id, @Valid EditAdminDataDTO adminDataDTO) {
    AdminData adminData = AccessLevelConverter.mapEditAdminDataDtoToAdminData(adminDataDTO);
    Account account = accountManager.editAccessLevel(id, adminData, adminDataDTO.getVersion());
//        repeatTransaction(accountManager, () -> accountManager.editAccessLevel(id, adminData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/chemist")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO editChemistData(
      @PathParam("id") Long id, @Valid EditChemistDataDTO chemistDataDTO) {
    ChemistData chemistData =
        AccessLevelConverter.mapEditChemistDataDtoToChemistData(chemistDataDTO);
    Account account = accountManager.editAccessLevel(id, chemistData, chemistDataDTO.getVersion());
//        repeatTransaction(accountManager, () -> accountManager.editAccessLevel(id, chemistData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/grantPatient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO grantPatient(
      @PathParam("id") Long id, @Valid GrantPatientDataDTO patientDataDTO) {
    PatientData patientData =
        AccessLevelConverter.mapGrantPatientDataDTOtoPatientData(patientDataDTO);
    Account account = accountManager.grantAccessLevel(id, patientData);
    //        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id,
    // patientData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/grantChemist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO grantChemist(
      @PathParam("id") Long id, @Valid GrantChemistDataDTO chemistDataDTO) {
    ChemistData chemistData =
        AccessLevelConverter.mapGrantChemistDataDtoToChemistData(chemistDataDTO);
    Account account = accountManager.grantAccessLevel(id, chemistData);
    //        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id,
    // chemistData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/grantAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public AccountAndAccessLevelsDTO grantAdmin(
      @PathParam("id") Long id, @Valid GrantAdminDataDTO adminDataDTO) {
    AdminData adminData = AccessLevelConverter.mapGrantAdminDataDtoToAdminData(adminDataDTO);
    Account account = accountManager.grantAccessLevel(id, adminData);
    //        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id,
    // adminData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/add-patient")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPatientAccountAsAdmin(
      @NotNull @Valid AddPatientAccountDto addPatientAccountDto) {
    Account account = AccountConverter.mapAddPatientDtoToAccount(addPatientAccountDto);
    accountManager.registerAccount(account);
    //    repeatTransaction(accountManager, () -> accountManager.registerAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-chemist")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addChemistAccountAsAdmin(
      @NotNull @Valid AddChemistAccountDto addChemistAccountDto) {
    Account account = AccountConverter.mapChemistDtoToAccount(addChemistAccountDto);
    accountManager.registerAccount(account);
    //    repeatTransaction(accountManager, () -> accountManager.registerAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-admin")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addAdminAccountAsAdmin(@NotNull @Valid AddAdminAccountDto addAdminAccountDto) {
    Account account = AccountConverter.mapAdminDtoToAccount(addAdminAccountDto);
    accountManager.registerAccount(account);
    //    repeatTransaction(accountManager, () -> accountManager.registerAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @DELETE
  @Path("/{id}/removeRoleAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response removeRoleAdmin(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
    Account account =
        accountManager.removeAccessLevel(
            id, AccessLevelConverter.mapAdminDataDtoToAdminData(adminDataDTO));
    //        repeatTransaction(
    //            accountManager,
    //            () ->
    //                accountManager.removeAccessLevel(
    //                    id, AccessLevelConverter.mapAdminDataDtoToAdminData(adminDataDTO)));
    return Response.status(Response.Status.OK)
        .entity(AccountConverter.mapAccountToAccountDto(account))
        .build();
  }

  @DELETE
  @Path("/{id}/removeRoleChemist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response removeRoleChemist(
      @PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
    Account account =
        accountManager.removeAccessLevel(
            id, AccessLevelConverter.mapChemistDataDtoToChemistData(chemistDataDTO));
    //        repeatTransaction(
    //            accountManager,
    //            () ->
    //                accountManager.removeAccessLevel(
    //                    id, AccessLevelConverter.mapChemistDataDtoToChemistData(chemistDataDTO)));
    return Response.status(Response.Status.OK)
        .entity(AccountConverter.mapAccountToAccountDto(account))
        .build();
  }

  @DELETE
  @Path("/{id}/removeRolePatient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response removeRolePatient(
      @PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
    Account account =
        accountManager.removeAccessLevel(
            id, AccessLevelConverter.mapPatientDataDtoToPatientData(patientDataDTO));
    //        repeatTransaction(
    //            accountManager,
    //            () ->
    //                accountManager.removeAccessLevel(
    //                    id, AccessLevelConverter.mapPatientDataDtoToPatientData(patientDataDTO)));
    return Response.status(Response.Status.OK)
        .entity(AccountConverter.mapAccountToAccountDto(account))
        .build();
  }
}
