package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AccountApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

public class AccountFacadeExceptionsInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        try {
            return invocationContext.proceed();
        } catch(OptimisticLockException e) {
            throw e;
        } catch(PersistenceException e) {
            // todo diversify exceptions
            throw AccountApplicationException.createAccountConstraintViolationException(e);
        }
    }
}
