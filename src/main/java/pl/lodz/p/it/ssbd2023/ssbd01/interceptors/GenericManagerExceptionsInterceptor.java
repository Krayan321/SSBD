package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

public class GenericManagerExceptionsInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext context) {
        try {
            return context.proceed();
        } catch(ApplicationException e) {
            throw e;
        } catch(EJBAccessException | AccessLocalException e) {
            throw ApplicationException.createAccessDeniedException();
        } catch(Exception e) {
            throw ApplicationException.createGeneralException(e);
        }
    }
}
