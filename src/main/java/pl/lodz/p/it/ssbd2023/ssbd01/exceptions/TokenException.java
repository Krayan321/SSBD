package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.EXPECTATION_FAILED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.ws.rs.core.Response;

@jakarta.ejb.ApplicationException(rollback = true)
public class TokenException extends ApplicationException {

    private TokenException(Response.Status status, String key) {
        super(status, key);
    }

    private TokenException(Response.Status status, String key, Exception e) {
        super(status, key, e);
    }

    public static TokenException tokenExpiredException() {
        throw new TokenException(EXPECTATION_FAILED, "Token expired");
    }

    public static TokenException tokenAlreadyUsedException() {
        throw new TokenException(EXPECTATION_FAILED, "Token already used");
    }

    public static TokenException tokenNotFoundException() {
        throw new TokenException(NOT_FOUND, "Token not found");
    }

    public static TokenException incorrectTokenTypeException() {
        throw new TokenException(EXPECTATION_FAILED, "Incorrect token type");
    }

}
