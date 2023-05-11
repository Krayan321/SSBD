package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

@Stateless
public class PrescriptionFacade extends AbstractFacade<Prescription> {
  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public PrescriptionFacade() {
    super(Prescription.class);
  }

  public List<Prescription> findAll() {
    TypedQuery<Prescription> tq = em.createNamedQuery("prescription.findAll", Prescription.class);
    return tq.getResultList();
  }
}
