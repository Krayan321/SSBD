package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;

import java.util.List;

public class CategoryManager extends AbstractManager implements CategoryManagerLocal, SessionSynchronization {

    @Inject private CategoryFacade categoryFacade;

    @Override
    @DenyAll
    public List<Category> getAllCategories() {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Category createCategory(Category category) {
        categoryFacade.create(category);
        return category;
    }

    @Override
    @DenyAll
    public Category getCategory(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Category editCategory(Category category) {
        throw new UnsupportedOperationException();
    }
}
