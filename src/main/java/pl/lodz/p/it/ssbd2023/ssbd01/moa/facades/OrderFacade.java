package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

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

    public List<Order> findAll() {
        TypedQuery<Order> tq = em.createNamedQuery("order.findAll", Order.class);
        return tq.getResultList();
    }
}
