package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("konto")
@RequestScoped
public class AccountController {

    @POST
    @Path("/client")
    public Response createClient() {
        return Response.status(Response.Status.CREATED).entity(null).build();
    }
}
