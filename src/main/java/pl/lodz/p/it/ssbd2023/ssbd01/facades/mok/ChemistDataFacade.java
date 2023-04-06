package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "ChemistDataFacade")
public class ChemistDataFacade extends AbstractFacade<ChemistData> implements ChemistDataFacadeLocal {

    @PersistenceContext(unitName = "ssbd01mok")
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
