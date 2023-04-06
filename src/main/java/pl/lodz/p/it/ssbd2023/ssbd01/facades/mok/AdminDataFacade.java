package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "AdminDataFacade")
public class AdminDataFacade extends AbstractFacade<AdminData> implements AdminDataFacadeLocal {
    @PersistenceContext(unitName = "ssbd01mok")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdminDataFacade() {
        super(AdminData.class);
    }

    public List<AdminData> findAll() {
        TypedQuery<AdminData> tq = em.createNamedQuery("adminData.findAll", AdminData.class);
        return tq.getResultList();
    }
}
