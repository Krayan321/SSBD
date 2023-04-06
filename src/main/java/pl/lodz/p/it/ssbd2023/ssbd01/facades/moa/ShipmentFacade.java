package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "ShipmentFacade")
public class ShipmentFacade extends AbstractFacade<Shipment> implements ShipmentFacadeLocal  {

    @PersistenceContext(unitName = "ssbd01moa")
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
