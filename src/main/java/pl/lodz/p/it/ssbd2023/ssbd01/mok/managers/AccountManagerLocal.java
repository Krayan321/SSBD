package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import com.mailjet.client.errors.MailjetException;
import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

import java.util.Date;
import java.util.List;

@Local
public interface AccountManagerLocal {


    Account createAccount(Account account, AccessLevel accessLevel);

    List<Account> getAllAccounts();

    Account findByLogin(String login);

    void confirmAccountRegistration(String token);

    Account getAccount(Long id);

    Account getAccountAndAccessLevels(Long id);

    Account registerAccount(Account account);

    Account editAccessLevel(Long id, AccessLevel accessLevel);

    Account grantAccessLevel(Long id, AccessLevel accessLevel);

    Account activateUserAccount(Long id);

    Account createPatientAccount(Account account, PatientData patientData);

    Account updateUserPassword(Long id, String newPassword);

    Account removeAccessLevel(Long id, AccessLevel accessLevel);

    Account updateOwnPassword(Long id, String oldPassword, String newPassword);

    void purgeUnactivatedAccounts();

    void updateAuthInformation(String caller, String remoteAddr, Date now, Boolean isCorrect) throws MailjetException;

    void sendVerificationTokenIfPreviousWasNotSent();
}
