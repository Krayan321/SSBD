package pl.lodz.p.it.ssbd2023.ssbd01.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountFacade extends AbstractFacade<Account>{

    @PersistenceContext(unitName = "ssbd01admin")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
}
