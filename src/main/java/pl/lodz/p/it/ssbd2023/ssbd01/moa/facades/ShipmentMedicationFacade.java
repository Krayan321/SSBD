package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;

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

  public List<ShipmentMedication> findAll() {
    TypedQuery<ShipmentMedication> tq =
        em.createNamedQuery("shipmentMedication.findAll", ShipmentMedication.class);
    return tq.getResultList();
  }
}
