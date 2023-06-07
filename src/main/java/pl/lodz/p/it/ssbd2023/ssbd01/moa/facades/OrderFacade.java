package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;

@Stateless
public class OrderFacade extends AbstractFacade<Order> {
  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public OrderFacade() {
    super(Order.class);
  }

  @Override
  @PermitAll
  public List<Order> findAll() {
    return super.findAll();
  }

  @Override
  @RolesAllowed("createOrder")
  public void create(Order order) {
    super.create(order);
  }

  @Override
  @PermitAll
  public void edit(Order order) {
    super.edit(order);
  }
  @Override
  @PermitAll
  public void editAndRefresh(Order order) {
    super.editAndRefresh(order);
  }
  @PermitAll
  public Optional<Order> find(Long id) {
    return super.find(id);
  }
}
