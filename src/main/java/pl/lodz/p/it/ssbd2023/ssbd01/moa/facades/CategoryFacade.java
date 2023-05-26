package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

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

  @Override
  @DenyAll
  public List<Category> findAll() {
    return super.findAll();
  }

  @Override
  @DenyAll
  public void create(Category category) {super.create(category);}

  @Override
  @DenyAll
  public void edit(Category category) {super.edit(category);}

  @Override
  @DenyAll
  public void editAndRefresh(Category category) {super.editAndRefresh(category);}

  @DenyAll
  public Optional<Category> find(Long id) {return super.find(id);}
}
