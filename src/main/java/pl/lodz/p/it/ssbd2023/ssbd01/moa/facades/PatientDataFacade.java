package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

@Stateless
public class PatientDataFacade extends AbstractFacade<PatientData> {

  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public PatientDataFacade() {
    super(PatientData.class);
  }
  @DenyAll
  public List<PatientData> findAll() {
    TypedQuery<PatientData> tq = em.createNamedQuery("patientData.findAll", PatientData.class);
    return tq.getResultList();
  }
}
