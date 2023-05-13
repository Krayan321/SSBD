package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import lombok.extern.java.Log;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

@Log
public class AccountFacadeExceptionsInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext invocationContext) throws Exception {
    try {
      return invocationContext.proceed();
    } catch (OptimisticLockException e) {
      throw e;
    } catch (PersistenceException | java.sql.SQLException e) {
      if (e.getCause() instanceof ConstraintViolationException csv) {
        PSQLException cause = (PSQLException) csv.getCause();
        if(cause.getMessage().contains("access_level_role")) {
          throw AccountApplicationException.createDuplicateAccessLevelException();
        } else if(cause.getMessage().contains("email")) {
          throw AccountApplicationException.createDuplicateEmailException();
        } else if(cause.getMessage().contains("login")) {
          throw AccountApplicationException.createDuplicateLoginException();
        }
      }
      throw AccountApplicationException.createAccountConstraintViolationException(e);
    }
  }
}
