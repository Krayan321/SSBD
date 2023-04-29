package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.List;

@Local
public interface AccountManagerLocal {
    Account createAccount(Account account, AccessLevel accessLevel);

    List<Account> getAllAccounts();

    Account getAccount(Long id);

    Account getAccountAndAccessLevel(Long id);

    // update, delete, activate, deactivate

    Account grantPatient(Long id, PatientData patientData);

    Account grantChemist(Long id, ChemistData chemistData);

    Account grantAdmin(Long id, AdminData adminData);
}
