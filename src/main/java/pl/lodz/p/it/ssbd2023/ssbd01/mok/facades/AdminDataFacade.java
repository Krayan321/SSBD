package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "AdminDataFacade")
public class AdminDataFacade extends AbstractFacade<AdminData> implements AdminDataFacadeLocal {
    @PersistenceContext(unitName = "ssbd01mokPU")
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