package pl.lodz.p.it.ssbd2023.ssbd01.exceptions.mappers;

import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.util.logging.Level;

@Provider
@Log
public class WebApplicationThrowableMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        try {
            throw throwable;
        } catch (WebApplicationException e) {
            return e.getResponse();
        } catch (EJBAccessException | AccessLocalException e) {
            return ApplicationException.createAccessDeniedException().getResponse();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Unknown error", throwable);
            return ApplicationException.createGeneralException(e).getResponse();
        }
    }
}
