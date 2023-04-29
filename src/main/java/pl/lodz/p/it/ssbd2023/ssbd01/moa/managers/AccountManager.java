package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.rmi.RemoteException;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager extends AbstractManager implements AccountManagerLocal, SessionSynchronization {
    @Override
    public Account createAccount(Account account, AccessLevel accessLevel) {
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public Account getAccount(Long id) {
        return null;
    }

    @Override
    public Account getAccountAndAccessLevel(Long id) {
        return null;
    }

    @Override
    public Account grantPatient(Long id, PatientData patientData) {
        return null;
    }

    @Override
    public Account grantChemist(Long id, ChemistData chemistData) {
        return null;
    }

    @Override
    public Account grantAdmin(Long id, AdminData adminData) {
        return null;
    }


}
