package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
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
  @DenyAll
  public List<Order> findAll() {
    return super.findAll();
  }

  @Override
  @DenyAll
  public void create(Order order) {
    super.create(order);
  }

  public List<Order> findAllByPatientId(Long id) {
    TypedQuery<Order> query = em.createNamedQuery("Order.findByPatientDataId", Order.class);
    query.setParameter("patientDataId", id);
    return query.getResultList();
  }

  public List<Order> findAllOrdersInQueueSortByOrderDate() {
    TypedQuery<Order> query = em.createNamedQuery("Order.findAllOrdersInQueueSortByOrderDate", Order.class);
    return query.getResultList();
  }

  @Override
  @DenyAll
  public void edit(Order order) {
    super.edit(order);
  }
  @Override
  @DenyAll
  public void editAndRefresh(Order order) {
    super.editAndRefresh(order);
  }
  @DenyAll
  public Optional<Order> find(Long id) {
    return super.find(id);
  }
}
