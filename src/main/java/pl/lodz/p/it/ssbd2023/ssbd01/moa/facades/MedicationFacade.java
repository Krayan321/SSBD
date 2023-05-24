package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

@Stateless
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

  public List<Medication> findAll() {
    TypedQuery<Medication> tq = em.createNamedQuery("medication.findAll", Medication.class);
    return tq.getResultList();
  }

  @Override
  public void create(Medication medication) { super.create(medication);}

  @Override
  public void edit(Medication medication) { super.edit(medication);}

  @Override
  public void editAndRefresh(Medication medication) { super.editAndRefresh(medication);}

  @Override
  public Optional<Medication> find(Object id) { return super.find(id);}
}
