package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;

import java.util.List;
import java.util.Optional;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@Stateful
@DenyAll
public class CategoryManager extends AbstractManager implements CategoryManagerLocal, SessionSynchronization {


    @Inject
    private CategoryFacade categoryFacade;

    @Override
    @RolesAllowed("getAllCategories")
    public List<Category> getAllCategories() {
        return categoryFacade.findAll();    }

    @Override
    @RolesAllowed("createCategory")
    public Category createCategory(Category category) {
//        if (categoryFacade.findByName(category.getName()) != null) {
//            throw ApplicationException.createCategoryAlreadyExistsException();
//        }
        categoryFacade.create(category);
        return category;
    }

    @Override
    @PermitAll
    public Category getCategory(Long id) {
        Optional<Category> category = categoryFacade.find(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw ApplicationException.createEntityNotFoundException();
        }
    }

    @Override
    public Category editCategory(Category category) {
        throw new UnsupportedOperationException();
    }

    @Override
    @PermitAll
    public Category findByName(String name) {
        return categoryFacade.findByName(name);
    }
}
