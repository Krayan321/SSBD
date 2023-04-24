package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import java.util.List;

@Stateless(name = "ChemistDataFacadeMoa")
public class ChemistDataFacade extends AbstractFacade<ChemistData> implements ChemistDataFacadeLocal {

    @PersistenceContext(unitName = "ssbd01moaPU")
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
