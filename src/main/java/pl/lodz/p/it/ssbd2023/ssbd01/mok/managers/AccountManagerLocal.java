package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.List;

@Local
public interface AccountManagerLocal {
    Account createAccount(Account account, AccessLevel accessLevel);

    List<Account> getAllAccounts();

    Account getAccount(Long id);

    Account getAccountAndAccessLevels(Long id);

    Account registerAccount(Account account);

    Account updateAccount(Long id, Account account);

    Account editAccessLevel(Long id, AccessLevel accessLevel);

    Account grantAccessLevel(Long id, AccessLevel accessLevel);

    Account activateUserAccount(Long id);

    Account createPatientAccount(Account account, PatientData patientData);

    Account updateUserPassword(Long id, String newPassword);

    Account removeAccessLevel(Long id, AccessLevel accessLevel);

    void purgeUnactivatedAccounts();

}
