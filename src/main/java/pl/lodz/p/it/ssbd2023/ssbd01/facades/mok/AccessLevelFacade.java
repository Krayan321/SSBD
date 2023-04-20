package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import java.util.List;

@Stateless(name = "AccessLevelFacade")
public class AccessLevelFacade extends AbstractFacade<AccessLevel> implements AccessLevelFacadeLocal  {
    @PersistenceContext(unitName = "ssbd01moa")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    public List<AccessLevel> findAll() {
        TypedQuery<AccessLevel> tq = em.createNamedQuery("accessLevel.findAll", AccessLevel.class);
        return tq.getResultList();
    }
}
