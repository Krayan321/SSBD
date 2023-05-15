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

  @Inject
  @ConfigProperty(name = "reset-password.token.expiration.hours")
  private int RESET_PASSWORD_TOKEN_EXPIRATION_HOURS;

  @Override
  public void sendVerificationToken(Account account, String code) {
    Token token =
        Token.builder()
            .account(account)
            .code(code == null ? RandomStringUtils.randomAlphanumeric(8) : code)
            .tokenType(TokenType.VERIFICATION)
            .wasPreviousTokenSent(false)
            .expirationDate(
                new Date(
                    Instant.now()
                        .plusSeconds((long) VERIFICATION_TOKEN_EXPIRATION_HOURS * 60 * 60)
                        .toEpochMilli()))
            .build();
    // todo czy napewno createdby/modifiedby przez account?
    token.setCreatedBy(account);

    tokenFacade.create(token);
    emailService.sendRegistrationEmail(account.getEmail(), account.getLogin(),
            account.getLanguage(), token.getCode());
  }

  @Override
  public void sendResetPasswordToken(Account account) {
    Token token = Token.builder()
            .account(account)
            .code(RandomStringUtils.randomAlphanumeric(8))
            .tokenType(TokenType.PASSWORD_RESET)
            .wasPreviousTokenSent(false)
            .expirationDate(new Date(Instant.now()
                    .plusSeconds((long) RESET_PASSWORD_TOKEN_EXPIRATION_HOURS * 60 * 60)
                    .toEpochMilli()))
            .build();

    token.setCreatedBy(account);

    tokenFacade.create(token);
    emailService.sendEmailResetPassword(account.getEmail(), account.getLogin(),
            account.getLanguage(), token.getCode());
  }

  @Override
  public void verifyAccount(String code) {
    Token token = tokenFacade.findByCode(code);
    checkIfTokenIsValid(token);
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
  public void setNewPassword(String token, String newPassword) {
    Token foundToken = tokenFacade.findByCode(token);
    checkIfTokenIsValid(foundToken);

    Account account = foundToken.getAccount();
    account.setPassword(newPassword);

    tokenFacade.edit(foundToken);
  }

  private void checkIfTokenIsValid(Token token) {
    if (token == null) {
      throw TokenException.tokenNotFoundException();
    }
    Account account = token.getAccount();

    if (token.getExpirationDate().before(new java.util.Date())) {
      throw tokenExpiredException();
    }
    if (token.isUsed() || account.getConfirmed()) {
      log.warning("MAGNO MANGO ZET Token already used");
      throw tokenAlreadyUsedException();
    }
    if (token.getTokenType() != TokenType.VERIFICATION) {
      throw incorrectTokenTypeException();
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
