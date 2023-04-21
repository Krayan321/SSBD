package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "ChemistDataFacadeMok")
public class ChemistDataFacade extends AbstractFacade<ChemistData> implements ChemistDataFacadeLocal {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChemistDataFacade() {
        super(ChemistData.class);
    }

    public List<ChemistData> findAll() {
        TypedQuery<ChemistData> tq = em.createNamedQuery("chemistData.findAll", ChemistData.class);
        return tq.getResultList();
    }
}
