package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;

@Stateless
public class ShipmentFacade extends AbstractFacade<Shipment> {

  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ShipmentFacade() {
    super(Shipment.class);
  }

  @Override
  public List<Shipment> findAll() {
    return super.findAll();
  }

  //idk todo
}
