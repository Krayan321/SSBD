package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.*;
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

  public static AccountApplicationException createAccountNotConfirmedException() {
    return new AccountApplicationException(
            UNAUTHORIZED, EXCEPTION_ACCOUNT_NOT_CONFIRMED);
  }

  public static AccountApplicationException createAccountBlockedException() {
    return new AccountApplicationException(
            UNAUTHORIZED, EXCEPTION_ACCOUNT_BLOCKED);
  }

  public static AccountApplicationException createUndefinedAccessLevelException() {
    return new AccountApplicationException(BAD_REQUEST, EXCEPTION_ACCOUNT_NO_SUCH_ACCESS_LEVEL);
  }

  public static AccountApplicationException createDuplicateAccessLevelException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_ACCESS_LEVEL);
  }

  public static AccountApplicationException createDuplicateEmailException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_EMAIL);
  }

  public static AccountApplicationException createDuplicateLoginException() {
    return new AccountApplicationException(CONFLICT, EXCEPTION_ACCOUNT_DUPLICATE_LOGIN);
  }
}
