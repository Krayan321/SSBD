package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "PatientDataFacade")
public class PatientDataFacade extends AbstractFacade<PatientData> implements PatientDataFacadeLocal {

    @PersistenceContext(unitName = "ssbd01mok")
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
