package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;


import static jakarta.ws.rs.core.Response.Status.CONFLICT;

@jakarta.ejb.ApplicationException(rollback = true)
public class ApplicationExceptionOptimisticLock extends ApplicationException {

    ApplicationExceptionOptimisticLock() {
        super(CONFLICT, "Optimistic lock exception");
    }
}
