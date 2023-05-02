package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.AccountFacade;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.security.HashAlgorithmImpl;
import pl.lodz.p.it.ssbd2023.ssbd01.util.AccessLevelFinder;
import pl.lodz.p.it.ssbd2023.ssbd01.util.mergers.AccessLevelMerger;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager implements AccountManagerLocal {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    @ConfigProperty(name = "unconfirmed.account.deletion.timeout.hours")
    private int UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS;

    @Override
    public List<Account> getAllAccounts() {
        return accountFacade.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        Optional<Account> optionalAccount = accountFacade.find(id);
        if(optionalAccount.isEmpty())
            return null; // todo throw
        return optionalAccount.get();
    }

    @Override
    public Account getAccountAndAccessLevels(Long id) {
        Optional<Account> optionalAccount = accountFacade.findAndRefresh(id);
        if(optionalAccount.isEmpty())
            return null; // todo throw
        return optionalAccount.get();
    }

    @Override
    public Account grantAccessLevel(Long id, AccessLevel accessLevel) {
        Account account = getAccount(id);
        accessLevel.setAccount(account);
        account.getAccessLevels().add(accessLevel);
        accountFacade.edit(account);
        return account;
    }


    @Override
    public Account createPatientAccount(Account account, PatientData patientData) {
        return createPatientAccount(account, patientData);
    }

    @Override
    public Account registerAccount(Account account) {
        account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
        accountFacade.create(account);
        return account;
    }

    // todo add modified by and modification date
    @Override
    public Account editAccessLevel(Long id, AccessLevel accessLevel) {
        Account account = getAccount(id);
        AccessLevel found = AccessLevelFinder.findAccessLevel(account, accessLevel);
        AccessLevelMerger.mergeAccessLevels(found, accessLevel);
        accountFacade.edit(account);
        return account;
    }

    @Override
    public Account activateUserAccount(Long id) {
        Account account = getAccount(id);
        // if(account.getActive() == true) {
        // return null;
        // }
        account.setActive(true);
        accountFacade.edit(account);
        return account;
    }

    @Override
    public Account updateUserPassword(Long id, String newPassword) {
        Account account = getAccount(id);
        account.setPassword(HashAlgorithmImpl.generate(newPassword));
        accountFacade.edit(account);
        return account;
    }

    @Override
    public void purgeUnactivatedAccounts() {
        List<Account> accountsToPurge = accountFacade.findConfirmedFalse();

        for (Account account : accountsToPurge) {
            LocalDateTime timeoutThreshold = LocalDateTime.now().minusHours(UNCONFIRMED_ACCOUNT_DELETION_TIMEOUT_HOURS);
            LocalDateTime creationDate = Instant.ofEpochMilli(account.getCreationDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            if(creationDate.isBefore(timeoutThreshold)) {
                accountFacade.remove(account);
            }
        }
    }

    @Override
    public Account createAccount(Account account, AccessLevel accessLevel) {
        account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
        accessLevel.setAccount(account);
        account.getAccessLevels().add(accessLevel);
        account.setCreatedBy(account);
        accountFacade.create(account);
        return account;
    }

    @Override
    public Account removeAccessLevel(Long id, AccessLevel accessLevel){
        Optional<Account> optionalAccount = accountFacade.find(id);
        if(optionalAccount.isEmpty())
            //todo throw
            return null;
        Account account = optionalAccount.get();
        account.getAccessLevels().remove(accessLevel);
        accountFacade.edit(account);
        return account;
    }
}
