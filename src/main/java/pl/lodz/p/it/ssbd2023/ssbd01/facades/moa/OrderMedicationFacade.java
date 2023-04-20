package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import java.util.List;

@Stateless(name = "OrderMedicationFacade")
public class OrderMedicationFacade extends AbstractFacade<OrderMedication> implements OrderMedicationFacadeLocal {
    @PersistenceContext(unitName = "ssbd01moa")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderMedicationFacade() {
        super(OrderMedication.class);
    }

    public List<OrderMedication> findAll() {
        TypedQuery<OrderMedication> tq = em.createNamedQuery("orderMedication.findAll", OrderMedication.class);
        return tq.getResultList();
    }
}
