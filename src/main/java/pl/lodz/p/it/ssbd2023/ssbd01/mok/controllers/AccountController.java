package pl.lodz.p.it.ssbd2023.ssbd01.mok.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.CreatePatientDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccessLevelConverter;
import pl.lodz.p.it.ssbd2023.ssbd01.util.converters.AccountConverter;


@Path("/account")
@RequestScoped
public class AccountController {

    @Inject
    AccountManagerLocal accountManager;

    @POST
    @Path("/patient")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createPatient(@Valid CreatePatientDto createPatientDto) {
        Account createdAccount = accountManager.createPatientAccount(
                AccountConverter.mapAccountDtoToAccount(createPatientDto.getAccountDTO(),
                        createPatientDto.getPassword()),
                AccessLevelConverter
                        .mapPatientDataDtoToPatientData(createPatientDto.getPatientDataDTO()));
        return Response.status(Response.Status.CREATED)
                .entity(createdAccount)
                .build();

    }


}
