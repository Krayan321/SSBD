package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import java.util.List;
import java.util.Optional;

@Stateless
public class OrderMedicationFacade extends AbstractFacade<OrderMedication> {
    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderMedicationFacade() {
        super(OrderMedication.class);
    }
}
