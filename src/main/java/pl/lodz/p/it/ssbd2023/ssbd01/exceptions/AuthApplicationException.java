package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import jakarta.ws.rs.core.Response;

@jakarta.ejb.ApplicationException(rollback = true)
public class AuthApplicationException extends ApplicationException {
    private AuthApplicationException(Response.Status status, String key) {
        super(status, key);
    }

    private AuthApplicationException(Response.Status status, String key, Exception e) {
        super(status, key, e);
    }

    // todo split for login and password
    public static AuthApplicationException createInvalidLoginOrPasswordException() {
        throw new AuthApplicationException(Response.Status.UNAUTHORIZED, "Invalid login or password");
    }
}
