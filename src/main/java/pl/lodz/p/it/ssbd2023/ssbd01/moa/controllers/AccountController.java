package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("account")
@RequestScoped
public class AccountController {

    @POST
    public Response createClient() {
        return Response.status(Response.Status.CREATED).entity(null).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllClients() {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readClient(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).entity(id).build();
    }
}
