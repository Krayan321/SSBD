package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import java.util.List;

@Stateless(name = "ShipmentMedicationFacade")
public class ShipmentMedicationFacade extends AbstractFacade<ShipmentMedication> implements ShipmentMedicationFacadeLocal {
    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShipmentMedicationFacade() {
        super(ShipmentMedication.class);
    }

    public List<ShipmentMedication> findAll() {
        TypedQuery<ShipmentMedication> tq = em.createNamedQuery("shipmentMedication.findAll", ShipmentMedication.class);
        return tq.getResultList();
    }
}
