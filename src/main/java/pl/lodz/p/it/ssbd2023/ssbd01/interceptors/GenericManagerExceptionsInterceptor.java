package pl.lodz.p.it.ssbd2023.ssbd01.interceptors;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

@Log
public class GenericManagerExceptionsInterceptor {

  @AroundInvoke
  public Object intercept(InvocationContext context) throws ApplicationException {
    try {
      return context.proceed();
    } catch (ApplicationException e) {
      throw e;
    } catch (EJBAccessException | AccessLocalException e) {
      throw ApplicationException.createAccessDeniedException();
    } catch (Exception e) {
      // todo diversify exceptions
      log.warning(e.getMessage());
      throw ApplicationException.createGeneralException(e);
    }
  }
}
