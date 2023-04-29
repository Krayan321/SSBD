package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;

import javax.print.attribute.standard.Media;

@Path("account")
@RequestScoped
public class AccountController {

    @Inject
    private AccountManagerLocal accountManager;

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

    @PUT
    @Path("/{id}/grantPatient")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantPatient(@PathParam("id") Long id, @Valid PatientDataDTO patientDataDTO) {
        return Response.status(Response.Status.OK).entity(
                AccountConverter.dtoFromAccount(accountManager.grantPatient(id,
                        AccountConverter.dtoToPatientData(patientDataDTO)))
        ).build();
    }

    @PUT
    @Path("/{id}/grantChemist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantChemist(@PathParam("id") Long id, @Valid ChemistDataDTO chemistDataDTO) {
        return Response.status(Response.Status.OK).entity(id).build();
    }

    @PUT
    @Path("/{id}/grantAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantAdmin(@PathParam("id") Long id, @Valid AdminDataDTO adminDataDTO) {
        return Response.status(Response.Status.OK).entity(id).build();
    }
}
