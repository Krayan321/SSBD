package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.sql.SQLException;

public class GenericFacadeExceptionsInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch(OptimisticLockException e) {
            throw ApplicationException.createOptimisticLockException();
        } catch(PersistenceException | SQLException e) {
            throw ApplicationException.createPersistenceException(e);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw ApplicationException.createGeneralException(e);
        }
    }
}
