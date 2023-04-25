package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "ShipmentFacade")
public class ShipmentFacade extends AbstractFacade<Shipment> implements ShipmentFacadeLocal  {

    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShipmentFacade() {
        super(Shipment.class);
    }

    public List<Shipment> findAll() {
        TypedQuery<Shipment> tq = em.createNamedQuery("account.findAll", Shipment.class);
        return tq.getResultList();
    }
}
