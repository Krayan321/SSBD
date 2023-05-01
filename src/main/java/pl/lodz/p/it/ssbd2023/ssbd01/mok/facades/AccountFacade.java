package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class AccountFacade extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    public List<Account> findAll() {
        return super.findAll();
    }

    public Optional<Account> findAndRefresh(Long id) {
        return super.findAndRefresh(id);
    }

    public Account findByLogin(String login) {
        TypedQuery<Account> tq = em.createNamedQuery("account.findByLogin", Account.class);
        tq.setParameter(1, login);
        return tq.getSingleResult();
    }

    public List<Account> findConfirmedFalse() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Account> root = cq.from(Account.class);
        cq.select(root).where(cb.isFalse(root.get("confirmed")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public void edit(Account account) {
        super.edit(account);
    }

    @Override
    public void create(Account account) {
        super.create(account);
    }

    @Override
    public void remove(Account account) {
        super.remove(account);
    }

    public Optional<Account> find(Long id) {
        return super.find(id);
    }

}
