package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
