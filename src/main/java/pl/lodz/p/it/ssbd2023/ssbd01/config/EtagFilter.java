package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.ExceptionConverter;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.PRECONDITION_FAILED;

@Provider
@ETagFilterBinding
public class EtagFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String header = requestContext.getHeaderString("If-Match");
        if (header == null || header.isEmpty()) {
            ApplicationException e = ApplicationException.createEtagEmptyException();
            Response response = ExceptionConverter.mapApplicationExceptionToResponse(e);
            requestContext.abortWith(response);
        } else if (!EntityIdentitySignerVerifier.validateEntitySignature(header)) {
            ApplicationException e = ApplicationException.createEtagNotValidException();
            Response response = ExceptionConverter.mapApplicationExceptionToResponse(e);
            requestContext.abortWith(response);
        }
    }



}
