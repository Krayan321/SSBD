package pl.lodz.p.it.ssbd2023.ssbd01.security;

import com.mailjet.client.errors.MailjetException;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.Date;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.TokenDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationExceptionEntityNotFound;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AuthApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;

@Log
@Path("/auth")
public class AuthController extends AbstractController {

  @Inject private IdentityStoreHandler identityStoreHandler;

  @Inject private JwtUtils jwtUtils;
  @Context private HttpServletRequest httpServletRequest;
  @Inject private AccountManagerLocal accountManager;

  @POST
  @Path("/login")
  public Response authenticate(@Valid LoginDTO loginDto) {
    Account account;
    try {
      account = repeatTransaction(accountManager, () -> accountManager.findByLogin(loginDto.getLogin()));
    } catch (ApplicationExceptionEntityNotFound e) {
      throw AuthApplicationException.createInvalidLoginOrPasswordException();
    }

    if (!account.getConfirmed()) {
        throw AccountApplicationException.createAccountNotConfirmedException();
    }

    UsernamePasswordCredential credential =
        new UsernamePasswordCredential(loginDto.getLogin(), loginDto.getPassword());
    CredentialValidationResult result = identityStoreHandler.validate(credential);
    if (result.getStatus() != CredentialValidationResult.Status.VALID) {
      repeatTransactionVoid(
          accountManager,
          () ->
              accountManager.updateAuthInformation(
                  credential.getCaller(),
                  httpServletRequest.getRemoteAddr(),
                  Date.from(Instant.now()),
                  false));
      throw AuthApplicationException.createInvalidLoginOrPasswordException();
    }

    repeatTransactionVoid(
        accountManager,
        () ->
            accountManager.updateAuthInformation(
                credential.getCaller(),
                httpServletRequest.getRemoteAddr(),
                Date.from(Instant.now()),
                true));
    return Response.ok(new TokenDto(jwtUtils.create(result))).build();
  }
}
