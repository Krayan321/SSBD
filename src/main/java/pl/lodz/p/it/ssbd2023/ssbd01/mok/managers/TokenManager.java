package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.incorrectTokenTypeException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenAlreadyUsedException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenExpiredException;

import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.util.email.EmailService;

@Stateful
@Log
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericManagerExceptionsInterceptor.class})
public class TokenManager extends AbstractManager
    implements TokenManagerLocal, SessionSynchronization {

  @Inject TokenFacade tokenFacade;

  @Inject EmailService emailService;

  @Inject
  @ConfigProperty(name = "verification.token.expiration.hours")
  private int VERIFICATION_TOKEN_EXPIRATION_HOURS;

  @Override
  public void sendVerificationToken(Account account, String code) {
    Token token = makeToken(account, code, TokenType.VERIFICATION);
    tokenFacade.create(token);
    emailService.sendRegistrationEmail(account.getEmail(), account.getLogin(), token.getCode());
  }

  private Token makeToken(Account account, String code, TokenType tokenType) {
    Token token =
        Token.builder()
            .account(account)
            .code(code == null ? RandomStringUtils.randomAlphanumeric(8) : code)
            .tokenType(tokenType)
            .wasPreviousTokenSent(false)
            .expirationDate(createExpirationDate())
            .build();
    // todo czy napewno createdby/modifiedby przez account?
    token.setCreatedBy(account);
    token.setModifiedBy(account);

    return token;
  }

  private Date createExpirationDate() {
    return new Date(
        Instant.now()
            .plusSeconds((long) VERIFICATION_TOKEN_EXPIRATION_HOURS * 60 * 60)
            .toEpochMilli());
  }

  @Override
  public void verifyAccount(String code) {
    Token token = tokenFacade.findByCode(code);
    checkIfTokenIsValid(token, TokenType.VERIFICATION);
    Account account = token.getAccount();
    account.setConfirmed(true);
    account.setModifiedBy(account);
    account.setCreatedBy(account);
    token.setUsed(true);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
    tokenFacade.edit(token);
  }

  @Override
  public void sendEmailChangeEmail(Account account, String new_email) {
    Token token = makeToken(account, encodeEmail(new_email), TokenType.EMAIL_CHANGE_CONFIRM);
    tokenFacade.create(token);
    emailService.sendEmailChangeEmail(new_email, account.getLogin(), token.getCode());
  }

  private String encodeEmail(String new_email) {
    return RandomStringUtils.randomAlphanumeric(8)
        + Base64.getEncoder().encodeToString(new_email.getBytes());
  }

  private String decodeEmail(String code) {
    return Base64.getDecoder().decode((code.substring(8, code.length())).getBytes()).toString();
  }

  @Override
  public void confirmEmailChange(String code) {
    Token token = tokenFacade.findByCode(code);
    checkIfTokenIsValid(token, TokenType.EMAIL_CHANGE_CONFIRM);
    Account account = token.getAccount();
    account.setEmail(decodeEmail(code));
    account.setModifiedBy(account);
    account.setCreatedBy(account);
    token.setUsed(true);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
    tokenFacade.edit(token);
  }

  private void checkIfTokenIsValid(Token token, TokenType tokenType) {
    if (token == null) {
      TokenException.tokenNotFoundException();
    }
    Account account = token.getAccount();

    if (token.getExpirationDate().before(new java.util.Date())) {
      tokenExpiredException();
    }
    if (token.isUsed() || account.getConfirmed()) {
      log.warning("MAGNO MANGO ZET Token already used");
      tokenAlreadyUsedException();
    }
    if (token.getTokenType() != tokenType) {
      incorrectTokenTypeException();
    }
  }

  @Override
  public Token findByAccountId(Long id) {
    return tokenFacade.findByAccount(id);
  }

  @Override
  public void removeVerificationCode(Token token) {
    tokenFacade.remove(token);
  }

  @Override
  public List<Token> findTokensByTokenTypeAndExpirationDateBefore(
      TokenType verification, Date halfExpirationDate) {
    return tokenFacade.findByTypeAndBeforeGivenData(verification, halfExpirationDate);
  }
}
