package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.AuthApplicationException.accountBlockedException;

import com.mailjet.client.errors.MailjetException;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.security.HashAlgorithmImpl;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;
import pl.lodz.p.it.ssbd2023.ssbd01.util.email.EmailService;
import pl.lodz.p.it.ssbd2023.ssbd01.util.mergers.AccessLevelMerger;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@Log
@DenyAll
public class AccountManager extends AbstractManager implements AccountManagerLocal {

  @Inject private AccountFacade accountFacade;

  @Inject private TokenManagerLocal verificationManager;

  @Inject
  @ConfigProperty(name = "unconfirmed.account.deletion.timeout.hours")
  private int UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS;

  @Inject
  @ConfigProperty(name = "max.incorrect.login.attempts")
  private int MAX_INCORRECT_LOGIN_ATTEMPTS;

  @Inject
  @ConfigProperty(name = "temporary.account.block.hours")
  private int TEMPORARY_ACCOUNT_BLOCK_HOURS;

  @Inject private EmailService emailService;
  @Context
  private SecurityContext context;

  @Override
  @RolesAllowed("getAllAccounts")
  public List<Account> getAllAccounts() {
    return accountFacade.findAll();
  }

  @Override
  @PermitAll
  public Account findByLogin(String login) {
    // return accountFacade.findByLogin(login).orElseThrow(() ->
    // ApplicationException.createEntityNotFoundException());
    return accountFacade.findByLogin(login);
  }

  @Override
  @RolesAllowed("getCurrentUser")
  public Account getCurrentUser(){
    Long id = accountFacade.findByLogin(context.getUserPrincipal().getName()).getId();
    return getAccountAndAccessLevels(id);
  }

  @Override
  @RolesAllowed("getAccountAndAccessLevels")
  public Account getAccountAndAccessLevels(Long id) {
    Optional<Account> optionalAccount = accountFacade.findAndRefresh(id);
    if (optionalAccount.isEmpty()) {
      throw ApplicationException.createEntityNotFoundException();
    }
    return optionalAccount.get();
  }

  @Override
  @RolesAllowed("grantAccessLevel")
  public Account grantAccessLevel(Long id, AccessLevel accessLevel) {
    Account account = getAccount(id);
    accessLevel.setAccount(account);
    account.getAccessLevels().add(accessLevel);
    accountFacade.editAndRefresh(account);
    return account;
  }

  @Override
  @RolesAllowed("getAccount")
  public Account getAccount(Long id) {
    Optional<Account> optionalAccount = accountFacade.find(id);
    if (optionalAccount.isEmpty()) {
      throw ApplicationException.createEntityNotFoundException();
    }
    return optionalAccount.get();
  }

  @Override
  @PermitAll
  public void confirmAccountRegistration(String verificationToken) {
    verificationManager.verifyAccount(verificationToken);
  }

  @Override
  @PermitAll
  public Account registerAccount(Account account) {
    account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
    accountFacade.create(account);
    verificationManager.sendVerificationToken(account, null);
    return account;
  }

  @Override
  @RolesAllowed("editAccessLevel")
  public Account editAccessLevel(Long id, AccessLevel accessLevel, Long version) {
    Account account = getAccount(id);
    AccessLevel found = AccessLevelFinder.findAccessLevel(account, accessLevel);
    if(!Objects.equals(found.getVersion(), version)) {
      throw ApplicationException.createOptimisticLockException();
    }
    AccessLevelMerger.mergeAccessLevels(found, accessLevel);
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("activateUserAccount")
  public Account activateUserAccount(Long id) {
    Account account = getAccount(id);
    account.setConfirmed(true);
    emailService.sendEmailAccountActivated(
        account.getEmail(), account.getLogin(), account.getLanguage());
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("blockAccount")
  public void blockAccount(Long id) {
    Account account = getAccount(id);
    if (!account.getActive()) {
      return;
    }
    account.setActive(false);
    accountFacade.edit(account);
    emailService.sendEmailAccountBlocked(
        account.getEmail(), account.getLogin(), account.getLanguage());
  }

  @Override
  @RolesAllowed("unblockAccount")
  public void unblockAccount(Long id) {
    Account account = getAccount(id);
    if (account.getActive()) {
      return;
    }
    account.setActive(true);
    accountFacade.edit(account);
    emailService.sendEmailAccountUnblocked(
        account.getEmail(), account.getLogin(), account.getLanguage());
  }

  @Override
  @RolesAllowed("updateUserPassword")
  public Account updateUserPassword(Long id, String newPassword) {
    Account account = getAccount(id);
    account.setPassword(HashAlgorithmImpl.generate(newPassword));
    accountFacade.edit(account);
    return account;
  }

  @Override
  @RolesAllowed("updateOwnPassword")
  public Account updateOwnPassword(Long id, String oldPassword, String newPassword) {
    Account account = getAccount(id);
    if (HashAlgorithmImpl.check(oldPassword, account.getPassword())) {
      account.setPassword(HashAlgorithmImpl.generate(newPassword));
      accountFacade.edit(account);
      return account;
    } else {
      return null;
    }
  }

  @Override
  @RolesAllowed("updateOwnEmail")
  public Account updateOwnEmail(Long id, String email) {
    Account account = getAccount(id);
    account.setEmail(email); // check validity??
    accountFacade.edit(account);
    return account;
  }

  @Override
  @DenyAll
  public void purgeUnactivatedAccounts() {
    List<Account> accountsToPurge = accountFacade.findNotConfirmed();

    for (Account account : accountsToPurge) {
      LocalDateTime timeoutThreshold =
          LocalDateTime.now().minusHours(UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS);
      LocalDateTime creationDate =
          Instant.ofEpochMilli(account.getCreationDate().getTime())
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      if (creationDate.isBefore(timeoutThreshold)) {
        emailService.sendEmailWhenRemovedDueToNotConfirmed(account.getEmail(), account.getLogin());
        accountFacade.remove(account);
      }
    }
  }

  @Override
  @PermitAll
  public void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect) {
    Account account = accountFacade.findByLogin(caller);
    if (account.getLoginAttempts() >= MAX_INCORRECT_LOGIN_ATTEMPTS) {
      LocalDateTime timeoutThreshold =
          LocalDateTime.now().minusHours(TEMPORARY_ACCOUNT_BLOCK_HOURS);
      LocalDateTime lastIncorrectLogin =
          Instant.ofEpochMilli(account.getLastNegativeLogin().getTime())
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      if (lastIncorrectLogin.isBefore(timeoutThreshold)) {
        account.setActive(true);
      }
    }

    if (!account.getActive()) {
      throw accountBlockedException();
    }
    if (isCorrect) {
      account.setLastPositiveLogin(now);
      account.setLogicalAddress(remoteAddr);
      account.setLoginAttempts(0);
    } else {
      account.setLastNegativeLogin(now);
      account.setLogicalAddress(remoteAddr);
      account.setLoginAttempts(account.getLoginAttempts() + 1);
      if (account.getLoginAttempts() >= MAX_INCORRECT_LOGIN_ATTEMPTS) {
        account.setActive(false);
        emailService.sendEmailAccountBlockedTooManyLogins(account.getEmail(), account.getLogin(),
                account.getLanguage());
      }
    }
  }

  @Override
  @DenyAll
  public void sendVerificationTokenIfPreviousWasNotSent() {

    Date halfExpirationDate =
        Date.from(
            Instant.now().minus(UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS / 2, ChronoUnit.HOURS));

    List<Token> tokensToResend =
        verificationManager.findTokensByTokenTypeAndExpirationDateBefore(
            TokenType.VERIFICATION, halfExpirationDate);

    tokensToResend.forEach(
        token -> {
          token.setWasPreviousTokenSent(true);
          verificationManager.sendVerificationToken(token.getAccount(), token.getCode());
        });
  }

  // fixme? Is this really necessary
  @Override
  //todo
  public Account createAccount(Account account, AccessLevel accessLevel) {
    account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
    accessLevel.setAccount(account);
    account.getAccessLevels().add(accessLevel);
    account.setCreatedBy(account);
    accountFacade.create(account);
    return account;
  }

  @Override
  @RolesAllowed("removeAccessLevel")
  public Account removeAccessLevel(Long id, AccessLevel accessLevel) {
    Account account = getAccount(id);
    account.getAccessLevels().remove(accessLevel);
    accountFacade.edit(account);
    return account;
  }
}
