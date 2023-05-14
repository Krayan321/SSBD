package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import com.mailjet.client.errors.MailjetException;
import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

@Local
public interface AccountManagerLocal extends CommonManagerLocalInterface {

  Account createAccount(Account account, AccessLevel accessLevel);

  List<Account> getAllAccounts();

  Account findByLogin(String login);

  void confirmAccountRegistration(String token);

  Account getAccount(Long id);
  Account getCurrentUser();

  Account getAccountAndAccessLevels(Long id);

  Account registerAccount(Account account);

  Account editAccessLevel(Long id, AccessLevel accessLevel, Long version);

  Account grantAccessLevel(Long id, AccessLevel accessLevel);

  Account activateUserAccount(Long id);

  void blockAccount(Long id);

  void unblockAccount(Long id);

  Account updateUserPassword(Long id, String newPassword);

  Account removeAccessLevel(Long id, AccessLevel accessLevel);

  Account updateOwnPassword(Long id, String oldPassword, String newPassword);

  Account updateOwnEmail(Long id, String email);

  void purgeUnactivatedAccounts();

  void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect);

  void sendVerificationTokenIfPreviousWasNotSent();
}
