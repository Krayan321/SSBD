package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericFacadeExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TokenFacadeInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({
        GenericFacadeExceptionsInterceptor.class,
        TokenFacadeInterceptor.class,
        TrackerInterceptor.class
})
public class TokenFacade extends AbstractFacade<Token> {
  @PersistenceContext(unitName = "ssbd01mokPU")
  private EntityManager em;

  public TokenFacade() {
    super(Token.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public void edit(Token token) {
    super.edit(token);
  }

  @Override
  public void editAndRefresh(Token token) {
    super.editAndRefresh(token);
  }

  public Token findByAccount(Long id) {
    TypedQuery<Token> tq = em.createNamedQuery("token.findByAccountId", Token.class);
    tq.setParameter(1, id);
    return tq.getSingleResult();
  }

  public List<Token> findByTypeAndBeforeGivenData(TokenType type, Date date) {
    TypedQuery<Token> tq =
        em.createNamedQuery(
            "token.findByTypeAndBeforeGivenDataAndNotUsedAndNotPreviouslySent", Token.class);
    tq.setParameter(1, type);
    tq.setParameter(2, date);
    return tq.getResultList();
  }

  public Token findByCode(String code) {
    TypedQuery<Token> tq = em.createNamedQuery("token.findByCode", Token.class);
    tq.setParameter(1, code);
    return tq.getSingleResult();
  }

  @Override
  public void create(Token token) {
    super.create(token);
  }

  @Override
  public void remove(Token token) {
    super.remove(token);
  }
}
