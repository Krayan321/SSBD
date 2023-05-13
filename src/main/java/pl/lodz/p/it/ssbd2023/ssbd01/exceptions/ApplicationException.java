package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationException extends WebApplicationException {

  // todo add internationalisation
  @Getter private Throwable cause;

  @Getter private final String key;

  protected ApplicationException(Response.Status status, String key, Throwable cause) {
    super(status);
    this.key = key;
    this.cause = cause;
  }

  protected ApplicationException(Response.Status status, String key) {
    super(status);
    this.key = key;
  }

  public static ApplicationException createGeneralException(Throwable cause) {
    return new ApplicationException(INTERNAL_SERVER_ERROR, EXCEPTION_GENERAL, cause);
  }

  public static ApplicationException createPersistenceException(Exception cause) {
    return new ApplicationException(INTERNAL_SERVER_ERROR, EXCEPTION_PERSISTENCE, cause);
  }

  public static ApplicationException createAccessDeniedException() {
    return new ApplicationException(FORBIDDEN, EXCEPTION_ACCESS_DENIED);
  }

  public static ApplicationException createTransactionRolledBackException() {
    return new ApplicationException(INTERNAL_SERVER_ERROR, EXCEPTION_TRANSACTION_ROLLBACK);
  }

  public static ApplicationException createEntityNotFoundException() {
    return new ApplicationExceptionEntityNotFound();
  }

  public static ApplicationException createOptimisticLockException() {
    return new ApplicationExceptionOptimisticLock();
  }
}
