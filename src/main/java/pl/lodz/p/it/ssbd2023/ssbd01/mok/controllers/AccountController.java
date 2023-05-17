package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.config.ETagFilterBinding;
import pl.lodz.p.it.ssbd2023.ssbd01.config.EntityIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddPatientAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.NewPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.VerificationTokenDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccessLevelConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;

@Path("account")
@RequestScoped
@DenyAll
public class AccountController extends AbstractController {

  @Inject private AccountManagerLocal accountManager;

  @Inject private EntityIdentitySignerVerifier entityIdentitySignerVerifier;

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public List<AccountDTO> readAllClients() {
    List<Account> accounts =
        repeatTransaction(accountManager, () -> accountManager.getAllAccounts());
    return accounts.stream().map(AccountConverter::mapAccountToAccountDto).toList();
  }

  @POST
  @Path("/confirm")
  public Response confirmAccount(@Valid VerificationTokenDto token) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.confirmAccountRegistration(token.getToken()));
    return Response.ok().build();
  }

  @POST
  @Path("/confirmEmailChange")
  public Response confirmEmailChange(@Valid VerificationTokenDto token) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.confirmEmailChange(token.getToken()));
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readAccount(@PathParam("id") Long id) {
    Account account = repeatTransaction(accountManager, () -> accountManager.getAccount(id));
    AccountDTO accountDto = AccountConverter.mapAccountToAccountDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @GET
  @Path("/details")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readOwnAccount() {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.getCurrentUserWithAccessLevels());
    SelfAccountWithAccessLevelDTO accountDto =
        AccountConverter.mapAccountToSelfAccountWithAccessLevelsDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @GET
  @Path("/{id}/details")
  @Produces(MediaType.APPLICATION_JSON)
  public Response readAccountAndAccessLevels(@PathParam("id") Long id) {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.getAccountAndAccessLevels(id));
    AccountAndAccessLevelsDTO accountDto =
        AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
    String etag = entityIdentitySignerVerifier.calculateEntitySignature(accountDto);
    return Response.ok(accountDto).tag(etag).build();
  }

  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerPatientAccount(@NotNull @Valid RegisterPatientDTO registerPatientDto) {
    Account account = AccountConverter.mapRegisterPatientDtoToAccount(registerPatientDto);
    repeatTransaction(accountManager, () -> accountManager.registerAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Path("/{id}/block")
  public Response blockAccount(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.blockAccount(id));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{id}/unblock")
  public Response unblockAccount(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.unblockAccount(id));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("{id}/activate")
  public AccountDTO activateAccount(@PathParam("id") Long id) {
    Account account =
        repeatTransaction(accountManager, () -> accountManager.activateUserAccount(id));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/{id}/changeUserPassword")
  @ETagFilterBinding
  public AccountDTO changeUserPassword(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid UpdateOtherUserPasswordDTO updateOtherUserPasswordDTO) {
    String newPassword = updateOtherUserPasswordDTO.getPassword();
    Account account =
        repeatTransaction(accountManager, () -> accountManager.updateUserPassword(id, newPassword));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/change-password")
  @ETagFilterBinding
  public Response changePassword(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @Valid ChangePasswordDTO changePasswordDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(changePasswordDTO, etag);
    Account account = accountManager.getCurrentUser();
    String oldPassword = changePasswordDTO.getOldPassword();
    String newPassword = changePasswordDTO.getNewPassword();
    repeatTransaction(
        accountManager,
        () -> accountManager.updateOwnPassword(account.getId(), oldPassword, newPassword));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/")
  @ETagFilterBinding
  public AccountDTO editOwnAccount(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @Valid EditAccountDTO editAccountDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(editAccountDTO, etag);
    String email = editAccountDTO.getEmail();
    Account account = repeatTransaction(accountManager, () -> accountManager.updateOwnEmail(email));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/{id}")
  @ETagFilterBinding
  public AccountDTO editUserAccount(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid EditAccountDTO editAccountDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(editAccountDTO, etag);
    String email = editAccountDTO.getEmail();
    Account account =
        repeatTransaction(accountManager, () -> accountManager.updateUserEmail(id, email));
    return AccountConverter.mapAccountToAccountDto(account);
  }

  @PUT
  @Path("/reset-password")
  public Response resetPassword(@Valid ResetPasswordDTO resetPasswordDTO) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.sendResetPasswordToken(resetPasswordDTO.getEmail()));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/new-password")
  public Response setNewPassword(@Valid NewPasswordDTO newPasswordDTO) {
    repeatTransactionVoid(
        accountManager,
        () ->
            accountManager.setNewPassword(newPasswordDTO.getToken(), newPasswordDTO.getPassword()));
    return Response.status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{id}/patient")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editPatientData(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid EditPatientDataDTO patientDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(patientDataDTO, etag);
    PatientData patientData =
        AccessLevelConverter.mapEditPatientDataDtoToPatientData(patientDataDTO);
    Account account =
        repeatTransaction(
            accountManager,
            () -> accountManager.editAccessLevel(id, patientData, patientDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/admin")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editAdminData(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid EditAdminDataDTO adminDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(adminDataDTO, etag);
    AdminData adminData = AccessLevelConverter.mapEditAdminDataDtoToAdminData(adminDataDTO);
    Account account =
        repeatTransaction(
            accountManager,
            () -> accountManager.editAccessLevel(id, adminData, adminDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/chemist")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO editChemistData(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid EditChemistDataDTO chemistDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(chemistDataDTO, etag);
    ChemistData chemistData =
        AccessLevelConverter.mapEditChemistDataDtoToChemistData(chemistDataDTO);
    Account account =
        repeatTransaction(
            accountManager,
            () -> accountManager.editAccessLevel(id, chemistData, chemistDataDTO.getVersion()));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/add-patient")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPatientAccountAsAdmin(
      @NotNull @Valid AddPatientAccountDto addPatientAccountDto) {
    Account account = AccountConverter.mapAddPatientDtoToAccount(addPatientAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-chemist")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addChemistAccountAsAdmin(
      @NotNull @Valid AddChemistAccountDto addChemistAccountDto) {
    Account account = AccountConverter.mapChemistDtoToAccount(addChemistAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/add-admin")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addAdminAccountAsAdmin(@NotNull @Valid AddAdminAccountDto addAdminAccountDto) {
    Account account = AccountConverter.mapAdminDtoToAccount(addAdminAccountDto);
    repeatTransaction(accountManager, () -> accountManager.createAccount(account));
    return Response.status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/{id}/patient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantPatient(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantPatientDataDTO patientDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(patientDataDTO, etag);
    PatientData patientData =
        AccessLevelConverter.mapGrantPatientDataDTOtoPatientData(patientDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id, patientData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/{id}/chemist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantChemist(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantChemistDataDTO chemistDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(chemistDataDTO, etag);
    ChemistData chemistData =
        AccessLevelConverter.mapGrantChemistDataDtoToChemistData(chemistDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id, chemistData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @POST
  @Path("/{id}/admin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ETagFilterBinding
  public AccountAndAccessLevelsDTO grantAdmin(
      @HeaderParam("If-Match") @NotEmpty String etag,
      @PathParam("id") Long id,
      @Valid GrantAdminDataDTO adminDataDTO) {
    entityIdentitySignerVerifier.checkEtagIntegrity(adminDataDTO, etag);
    AdminData adminData = AccessLevelConverter.mapGrantAdminDataDtoToAdminData(adminDataDTO);
    Account account =
        repeatTransaction(accountManager, () -> accountManager.grantAccessLevel(id, adminData));
    return AccountConverter.mapAccountToAccountAndAccessLevelsDto(account);
  }

  @PUT
  @Path("/{id}/admin/block")
  public Response blockRoleAdmin(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.ADMIN));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/chemist/block")
  public Response blockRoleChemist(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.CHEMIST));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/patient/block")
  public Response blockRolePatient(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.deactivateAccessLevel(id, Role.PATIENT));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/admin/unblock")
  public Response unblockRoleAdmin(@PathParam("id") Long id) {
    repeatTransactionVoid(accountManager, () -> accountManager.activateAccessLevel(id, Role.ADMIN));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/chemist/unblock")
  public Response unblockRoleChemist(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.activateAccessLevel(id, Role.CHEMIST));
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @PUT
  @Path("/{id}/patient/unblock")
  public Response unblockRolePatient(@PathParam("id") Long id) {
    repeatTransactionVoid(
        accountManager, () -> accountManager.activateAccessLevel(id, Role.PATIENT));
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
