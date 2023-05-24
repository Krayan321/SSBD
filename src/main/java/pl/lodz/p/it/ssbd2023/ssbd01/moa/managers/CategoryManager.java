package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.SessionSynchronization;;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;

import java.util.List;

public class CategoryManager extends AbstractManager implements CategoryManagerLocal, SessionSynchronization {

    @Inject private CategoryFacade categoryFacade;

    @Override
    public List<Category> getAllCategories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Category createCategory(Category category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Category getCategory(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Category editCategory(Category category) {
        throw new UnsupportedOperationException();
    }
}
