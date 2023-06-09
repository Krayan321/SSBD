package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import jakarta.validation.ConstraintViolationException;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MedicationFacade extends AbstractFacade<Medication> {
  @PersistenceContext(unitName = "ssbd01moaPU")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public MedicationFacade() {
    super(Medication.class);
  }

  @DenyAll
  public List<Medication> findAll() {
    TypedQuery<Medication> tq = em.createNamedQuery("medication.findAll", Medication.class);
    return tq.getResultList();
  }

  @Override
  @PermitAll
  public void create(Medication medication) {
    super.create(medication);
  }

  @Override
  @DenyAll
  public void edit(Medication medication) { super.edit(medication);}

  @Override
  @DenyAll
  public void editAndRefresh(Medication medication) { super.editAndRefresh(medication);}

  @Override
  @PermitAll
  public Optional<Medication> find(Object id) { return super.find(id);}

  @PermitAll
  public Medication findByName(String name) {
    TypedQuery<Medication> tq = em.createNamedQuery("medication.findByName", Medication.class);
    tq.setParameter("name", name);
    return tq.getSingleResult();
  }
}
