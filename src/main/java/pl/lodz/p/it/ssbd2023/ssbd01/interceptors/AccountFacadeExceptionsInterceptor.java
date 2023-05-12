package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import lombok.extern.java.Log;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;

@Log
public class AccountFacadeExceptionsInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext invocationContext) throws Exception {
    try {
      return invocationContext.proceed();
    } catch (OptimisticLockException e) {
      throw e;
    } catch(ConstraintViolationException e) {
      if(e.getMessage().matches("duplicate key value violates unique constraint")) {
        throw AccountApplicationException.createDuplicateAccessLevelException();
      }
      throw AccountApplicationException.createAccountConstraintViolationException(e);
    } catch (PersistenceException e) {
      throw AccountApplicationException.createAccountConstraintViolationException(e);
    }
  }
}
