package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

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
  public List<Shipment> findAll() {
    return super.findAll();
  }

  @RolesAllowed({"readShipment", "updateShipment"})
  public Optional<Shipment> find(Long id) {
    return super.find(id);
  }

  @RolesAllowed("updateShipment")
  public void edit(Shipment shipment) {
    super.edit(shipment);
  }
}
