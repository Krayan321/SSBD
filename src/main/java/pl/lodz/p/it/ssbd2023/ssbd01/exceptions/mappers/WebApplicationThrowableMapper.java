package pl.lodz.p.it.ssbd2023.ssbd01.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.i18n;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ExceptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

@Provider
@Log
public class WebApplicationThrowableMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable throwable) {
    try {
      throw throwable;
    } catch (WebApplicationException e) {
      return getResponseFromWAException(e);
    } catch (EJBAccessException | AccessLocalException e) {
      WebApplicationException ex = ApplicationException.createAccessDeniedException();
      return getResponseFromWAException(ex);
    } catch (Throwable e) {
      log.log(Level.SEVERE, i18n.EXCEPTION_UNKNOWN, throwable);
      WebApplicationException ex = ApplicationException.createGeneralException(e);
      return getResponseFromWAException(ex);
    }
  }

  private Response getResponseFromWAException(WebApplicationException e) {
    return Response.status(e.getResponse().getStatus())
        .entity(new ExceptionDTO(e.getMessage()))
        .header("Content-Type", "application/json")
        .build();
  }
}
