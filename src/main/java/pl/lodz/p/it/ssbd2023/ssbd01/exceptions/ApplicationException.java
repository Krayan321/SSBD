package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationException extends WebApplicationException {

    // todo add internationalisation
    @Getter
    private Throwable cause;

    protected ApplicationException(Response.Status status, String key, Throwable cause) {
        super(Response.status(status).entity(key).build());
        this.cause = cause;
    }

    protected ApplicationException(Response.Status status, String key) {
        super(Response.status(status).entity(key).build());
    }



    public static ApplicationException createGeneralException(Throwable cause) {
        return new ApplicationException(INTERNAL_SERVER_ERROR, "General exception occurred");
    }

    public static ApplicationException createPersistenceException(Exception cause) {
        return new ApplicationException(INTERNAL_SERVER_ERROR, "Persistence exception occurred");
    }

    public static ApplicationException createAccessDeniedException() {
        return new ApplicationException(FORBIDDEN, "Access denied");
    }

    public static ApplicationException createTransactionRolledBackException() {
        return new ApplicationException(INTERNAL_SERVER_ERROR, "Transaction rolled back");
    }



    public static ApplicationException createEntityNotFoundException() {
        throw new ApplicationExceptionEntityNotFound();
    }

    public static ApplicationException createOptimisticLockException() {
        throw new ApplicationExceptionOptimisticLock();
    }
}
