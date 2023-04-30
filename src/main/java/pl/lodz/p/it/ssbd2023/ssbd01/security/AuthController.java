package pl.lodz.p.it.ssbd2023.ssbd01.security;

import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.LoginDto;

@Log
@Path("/auth")
public class AuthController {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private JwtUtils jwtProvider;

    @POST
    @Path("/login")
    public Response authenticate(@Valid LoginDto loginDto) throws AuthenticationException {
        UsernamePasswordCredential credential =
                new UsernamePasswordCredential(loginDto.getLogin(), loginDto.getPassword());
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        log.warning(result.getCallerGroups().toString());
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return Response.ok(jwtProvider.create(result)).build();
        }
        throw new AuthenticationException("Invalid login or password");

    }


}
