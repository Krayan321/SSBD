package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import java.util.List;

@Stateless(name = "PatientDataFacadeMoa")
public class PatientDataFacade extends AbstractFacade<PatientData> implements PatientDataFacadeLocal {

    @PersistenceContext(unitName = "ssbd01moa")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PatientDataFacade() {
        super(PatientData.class);
    }

    public List<PatientData> findAll() {
        TypedQuery<PatientData> tq = em.createNamedQuery("patientData.findAll", PatientData.class);
        return tq.getResultList();
    }

}
