package pl.lodz.p.it.ssbd2023.ssbd01.security;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.java.Log;

@RequestScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final String BEARER_HEADER = "Bearer ";

    @Inject
    JwtUtils jwtUtils;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse,
                                                HttpMessageContext httpMessageContext)
            throws AuthenticationException {

        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_HEADER)) {
            CallerPrincipal login = new CallerPrincipal("guest");
            Set<String> roles = new HashSet<>() {{
                add(Roles.ANONYMOUS);
            }};
            return httpMessageContext.notifyContainerAboutLogin(login, roles);
        }

        String jwtString = authorizationHeader.substring(BEARER_HEADER.length()).trim();

        try {
            Claims claims = jwtUtils.load(jwtString);
            CallerPrincipal login = new CallerPrincipal(claims.getSubject());
            Set<String> roles = new HashSet<>() {{
                add(claims.get("roles", String.class));
            }};
            return httpMessageContext.notifyContainerAboutLogin(login, roles);
        } catch (Exception e) {
            return httpMessageContext.responseUnauthorized();
        }
    }
}