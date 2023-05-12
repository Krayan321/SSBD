package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.EXPECTATION_FAILED;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

import jakarta.ws.rs.core.Response;

@jakarta.ejb.ApplicationException(rollback = true)
public class AccountApplicationException extends ApplicationException {

  private AccountApplicationException(Response.Status status, String key) {
    super(status, key);
  }

  private AccountApplicationException(Response.Status status, String key, Exception e) {
    super(status, key, e);
  }

  public static AccountApplicationException createAccountConstraintViolationException(Exception e) {
    return new AccountApplicationException(
        EXPECTATION_FAILED, EXCEPTION_ACCOUNT_CONSTRAINT_VIOLATION, e);
  }

  public static AccountApplicationException createConfirmedAccountDeletionException() {
    return new AccountApplicationException(
        EXPECTATION_FAILED, EXCEPTION_ACCOUNT_DELETION_CONFIRMED);
  }

  public static AccountApplicationException createUndefinedAccessLevelException() {
    return new AccountApplicationException(BAD_REQUEST, EXCEPTION_ACCOUNT_NO_SUCH_ACCESS_LEVEL);
  }
}
