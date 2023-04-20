package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "MedicationFacade")
public class MedicationFacade extends AbstractFacade<Medication> implements MedicationFacadeLocal {
    @PersistenceContext(unitName = "ssbd01moa")
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
}
