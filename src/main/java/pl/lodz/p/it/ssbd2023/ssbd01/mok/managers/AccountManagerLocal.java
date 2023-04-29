package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Local;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

@Local
public interface AccountManagerLocal {

    Account createPatientAccount(Account account, PatientData patientData);

    Account createAccount(Account account, AccessLevel accessLevel);


}
