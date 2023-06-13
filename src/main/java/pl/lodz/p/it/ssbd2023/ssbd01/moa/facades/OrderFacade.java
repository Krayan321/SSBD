package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState;

import java.util.List;
import java.util.Optional;

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
                .createQuery("select distinct o from Order o "
                        + "left join fetch o.orderMedications "
                        + "where o.orderState = pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.IN_QUEUE")
                .getResultList();
    }


    public List<Order> findAllOrdersInQueueSortByOrderDate() {
        TypedQuery<Order> query = em.createNamedQuery("Order.findAllOrdersStateInQueueSortByOrderDate", Order.class);
        return query.getResultList();
    }

    @RolesAllowed("getOrdersToApprove")
    public List<Order> findNotYetApproved() {
        return getEntityManager()
                .createQuery("select o from Order o "
                        + "left join fetch o.orderMedications "
                        + "where o.prescription is not null "
                        + "and o.orderState = pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderState.WAITING_FOR_CHEMIST_APPROVAL")
                .getResultList();
    }


    @RolesAllowed("deleteWaitingOrdersById")
    public void deleteWaitingOrdersById(Long id) {
        String orderQuery = "UPDATE Order o SET o.orderState = :newState "
                + "WHERE o.id = :orderId AND o.orderState = :currentState";

        getEntityManager()
                .createQuery(orderQuery)
                .setParameter("newState", OrderState.REJECTED_BY_CHEMIST)
                .setParameter("currentState", OrderState.IN_QUEUE)
                .setParameter("orderId", id)
                .executeUpdate();

        }


  @RolesAllowed("withdraw")
  public void withdrawOrder(Long id, Long userId){
      String updateStateQuery = "UPDATE Order o " +
              "SET o.orderState = :newState " +
              "WHERE o.id = :orderId " +
              "AND o.orderState = :currentState " +
              "AND o.patientData.id = :userId";

      getEntityManager()
              .createQuery(updateStateQuery)
              .setParameter("newState", OrderState.REJECTED_BY_PATIENT)
              .setParameter("currentState", OrderState.TO_BE_APPROVED_BY_PATIENT)
              .setParameter("orderId", id)
              .setParameter("userId", userId)
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
