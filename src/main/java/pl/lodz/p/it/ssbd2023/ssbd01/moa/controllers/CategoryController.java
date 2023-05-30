package pl.lodz.p.it.ssbd2023.ssbd01.moa.controllers;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractController;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.managers.CategoryManager;

import java.util.List;

@Path("category")
@RequestScoped
@DenyAll
public class CategoryController extends AbstractController {

    @Inject private CategoryManager categoryManager;

    //moa 22
    @GET
    @Path("/")
    @DenyAll
    @Produces(MediaType.APPLICATION_JSON)
    public List<CategoryDTO> getAllCategories() {
        throw new UnsupportedOperationException();
    }


    @GET
    @Path("/{id}")
    @DenyAll
    @Produces(MediaType.APPLICATION_JSON)
    public CategoryDTO getCategory(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }

    //moa 21
    @POST
    @Path("/add-category")
    @DenyAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCategory(@NotNull @Valid CategoryDTO categoryDto) {
        repeatTransaction(categoryManager, () -> categoryManager.createCategory(new Category(categoryDto.getName(), categoryDto.getIsOnPrescription())));
        return Response.status(Response.Status.CREATED).build();
    }

    //moa 23
    @PUT
    @DenyAll
    @Path("/{id}/edit-category")
    public CategoryDTO editCategory(@PathParam("id") Long id) {
        throw new UnsupportedOperationException();
    }
}
