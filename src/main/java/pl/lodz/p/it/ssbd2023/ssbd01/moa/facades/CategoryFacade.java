package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import java.util.List;

@Stateless(name = "CategoryFacade")
public class CategoryFacade extends AbstractFacade<Category> {
    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(Category.class);
    }

    public List<Category> findAll() {
        TypedQuery<Category> tq = em.createNamedQuery("category.findAll", Category.class);
        return tq.getResultList();
    }
}
