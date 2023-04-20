package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "PrescriptionFacade")
public class PrescriptionFacade extends AbstractFacade<Prescription> implements PrescriptionFacadeLocal {
    @PersistenceContext(unitName = "ssbd01moa")
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
