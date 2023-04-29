package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.*;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.util.HashGenerator;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.util.List;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager implements AccountManagerLocal {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private HashGenerator hashGenerator;

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
    public Account grantAccessLevel(Long id, AccessLevel accessLevel) {
        Optional<Account> optionalAccount = accountFacade.find(id);
        if (optionalAccount.isEmpty())
            return null; // todo throw
        Account account = optionalAccount.get();
        account.getAccessLevels().add(accessLevel);
        accountFacade.edit(account);
        return account;
    }


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
