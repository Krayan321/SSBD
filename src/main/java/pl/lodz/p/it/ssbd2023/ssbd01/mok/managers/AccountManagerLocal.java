package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@Local
public interface AccountManagerLocal extends CommonManagerLocalInterface {

  Account createAccount(Account account);

  List<Account> getAllAccounts();

  Account findByLogin(String login);

  void confirmAccountRegistration(String token);

  Account getAccount(Long id);
  Account getCurrentUser();

  String getCurrentUserLogin();

  Account getCurrentUserWithAccessLevels();

  Account getAccountAndAccessLevels(Long id);

  Account registerAccount(Account account);

  Account editAccessLevel(Long id, AccessLevel accessLevel, Long version);

  Account grantAccessLevel(Long id, AccessLevel accessLevel);

  void deactivateAccessLevel(Long id, Role role);

  void activateAccessLevel(Long id, Role role);

  Account activateUserAccount(Long id);

  void blockAccount(Long id);

  void unblockAccount(Long id);

  void sendResetPasswordToken(String email);

  void setNewPassword(String token, String newPassword);

  Account updateUserPassword(Long id, String newPassword);

  Account updateOwnPassword(Long id, String oldPassword, String newPassword);

  Account updateOwnEmail(String email);

  Account updateUserEmail(Long id, String email);

  void purgeUnactivatedAccounts();

  void activateBlockedAccounts();

  void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect);

  void sendVerificationTokenIfPreviousWasNotSent();
}
