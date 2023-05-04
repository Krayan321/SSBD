package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.EXPECTATION_FAILED;

@jakarta.ejb.ApplicationException(rollback = true)
public class AccountApplicationException extends ApplicationException {

    private AccountApplicationException(Response.Status status, String key) {
        super(status, key);
    }

    private AccountApplicationException(Response.Status status, String key, Exception e) {
        super(status, key, e);
    }

    public static AccountApplicationException createAccountConstraintViolationException(Exception e) {
        return new AccountApplicationException(EXPECTATION_FAILED, "Violated account constraints", e);
    }

    public static AccountApplicationException createConfirmedAccountDeletionException() {
        return new AccountApplicationException(EXPECTATION_FAILED, "Trying to delete confirmed account");
    }

    public static AccountApplicationException createUndefinedAccessLevelException() {
        return new AccountApplicationException(BAD_REQUEST, "Given access level was ill-defined");
    }
}
