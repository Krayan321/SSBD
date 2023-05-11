package pl.lodz.p.it.ssbd2023.ssbd01.security;

import com.mailjet.client.errors.MailjetException;
import jakarta.inject.Inject;
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
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;

@Log
@Path("/auth")
public class AuthController {

  @Inject private IdentityStoreHandler identityStoreHandler;

  @Inject private JwtUtils jwtUtils;
  @Context private HttpServletRequest httpServletRequest;
  @Inject private AccountManagerLocal accountManager;

  @POST
  @Path("/login")
  public Response authenticate(@Valid LoginDTO loginDto)
      throws MailjetException, AuthenticationException {

    UsernamePasswordCredential credential =
        new UsernamePasswordCredential(loginDto.getLogin(), loginDto.getPassword());
    CredentialValidationResult result = identityStoreHandler.validate(credential);

    Account account = accountManager.findByLogin(loginDto.getLogin());

    if (!account.getConfirmed()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity("Account not confirmed").build();
    }

    if (!account.getActive()) {
      return Response.status(Response.Status.UNAUTHORIZED).entity("Account not active").build();
    }

    log.warning(result.getCallerGroups().toString());
    if (result.getStatus() != CredentialValidationResult.Status.VALID) {

      accountManager.updateAuthInformation(
          credential.getCaller(),
          httpServletRequest.getRemoteAddr(),
          Date.from(Instant.now()),
          false);
      return Response.status(Response.Status.UNAUTHORIZED)
          .entity("Invalid login or password")
          .build();
    } else {
      accountManager.updateAuthInformation(
          credential.getCaller(),
          httpServletRequest.getRemoteAddr(),
          Date.from(Instant.now()),
          true);
      return Response.ok(jwtUtils.create(result)).build();
    }
  }
}
