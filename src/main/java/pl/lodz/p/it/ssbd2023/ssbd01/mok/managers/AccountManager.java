package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.util.HashGenerator;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager implements AccountManagerLocal {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private HashGenerator hashGenerator;

    @Override
    public Account createPatientAccount(Account account, PatientData patientData) {
        return createPatientAccount(account, patientData);
    }

    @Override
    public Account createAccount(Account account, AccessLevel accessLevel) {
        account.setPassword(hashGenerator.generateHash(account.getPassword()));
        accessLevel.setAccount(account);
        account.getAccessLevels().add(accessLevel);
        account.setCreatedBy(account);
        accountFacade.create(account);
        return account;
    }
}
