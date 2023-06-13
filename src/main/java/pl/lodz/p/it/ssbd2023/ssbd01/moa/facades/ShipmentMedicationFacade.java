package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;

import java.util.List;

@Stateless
public class ShipmentMedicationFacade extends AbstractFacade<ShipmentMedication> {
    @PersistenceContext(unitName = "ssbd01moaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShipmentMedicationFacade() {
        super(ShipmentMedication.class);
    }

    public ShipmentMedication findLatestByMedication(Long medicationId) {
        TypedQuery<ShipmentMedication> query = em.createNamedQuery("shipmentMedication.findLatestShipmentMedicationForGivenMedication", ShipmentMedication.class);
        query.setParameter("medicationId", medicationId);
        List<ShipmentMedication> shipmentMedications = query.getResultList();
        if (shipmentMedications.isEmpty())
            return null;
        else {
            return shipmentMedications.get(0);
        }

    }

}
