package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
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

  @RolesAllowed("createShipment")
  public void create(Shipment shipment) {
    super.create(shipment);
  }

  @RolesAllowed("readAllShipments")
  public List<Shipment> findAllAndRefresh() {
    return getEntityManager()
            .createQuery("select s from Shipment s left join fetch s.shipmentMedications")
            .getResultList();
  }

  @RolesAllowed("readShipment")
  public Optional<Shipment> findAndRefresh(Long id) {
    return super.findAndRefresh(id);
  }
}
