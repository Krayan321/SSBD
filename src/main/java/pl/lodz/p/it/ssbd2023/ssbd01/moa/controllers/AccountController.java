package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;

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

    @POST
    @Path("/register")
    public Response register() {
        return Response.status(Response.Status.CREATED).entity(null).build();
    }

    @PUT
    @Path("/deactivate")
    public Response deactivate(@Valid AccountDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/activate")
    public Response activate(@Valid AccountDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/changeUserPassword")
    public Response changeUserPassword(@Valid AccountDTO accountDTO, @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/changePassword")
    public Response changePassword(@Valid AccountDTO accountDTO, @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/resetPassword")
    public Response resetPassword(@Valid AccountDTO accountDTO) {
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @PUT
    @Path("/setNewPassword")
    public Response setNewPassword(@Valid AccountDTO accountDTO, @QueryParam("newPassword") String newPassword) {
        return Response.status(Response.Status.OK).entity(null).build();
    }
}
