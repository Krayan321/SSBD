package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
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

  public List<Order> findAllByPatientId(Long id) {
    TypedQuery<Order> query = em.createNamedQuery("Order.findByPatientDataId", Order.class);
    query.setParameter("patientDataId", id);
    return query.getResultList();
  }
  @RolesAllowed("getWaitingOrders")
  public List<Order> findWaitingOrders() {
    return getEntityManager()
            .createQuery("select o from Order o "
                    + "left join fetch o.orderMedications "
                    + "where o.inQueue = true")
            .getResultList();
  }

  @RolesAllowed("getOrdersToApprove")
  public List<Order> findNotYetApproved() {
    return getEntityManager()
            .createQuery("select o from Order o "
                    + "left join fetch o.orderMedications "
                    + "where o.prescription is not null "
                    + "and o.inQueue = false")
            .getResultList();
  }

  @RolesAllowed("deleteWaitingOrdersById")
  public void deleteWaitingOrdersById(Long id) {
    String sqlQuery = "DELETE FROM OrderMedication om "
            + "WHERE om.order.id = :orderId";

    getEntityManager()
            .createQuery(sqlQuery)
            .setParameter("orderId", id)
            .executeUpdate();

      String orderQuery = "DELETE FROM Order o "
              + "WHERE o.id = :orderId AND o.inQueue = true";

      getEntityManager()
              .createQuery(orderQuery)
              .setParameter("orderId", id)
              .executeUpdate();
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
