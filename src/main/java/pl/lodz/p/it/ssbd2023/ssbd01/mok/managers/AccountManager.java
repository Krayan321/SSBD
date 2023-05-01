package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.inject.Inject;
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

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager implements AccountManagerLocal {

    @Inject
    private AccountFacade accountFacade;

    private Properties applicationProperties;

    public AccountManager() {
        try {
            FileReader fr = new FileReader("META-INF/application.properties");
            applicationProperties = new Properties();
            applicationProperties.load(fr);
        } catch(IOException e) {
            // todo throw
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountFacade.findAll();
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
    public Account registerAccount(Account account) {
        account.setPassword(HashAlgorithmImpl.generate(account.getPassword()));
        accountFacade.create(account);
        return account;
    }

    @Override
    public Account updateUserPassword(Long id,  String newPassword) {
        Optional<Account> optionalAccount = accountFacade.find(id);
        if (optionalAccount.isEmpty())
            return null; // todo throw
        Account account = optionalAccount.get();
        account.setPassword(HashAlgorithmImpl.generate(newPassword));
        accountFacade.edit(account);
        return account;
    }

    @Override
    public void purgeUnactivatedAccounts() {
        List<Account> accountsToPurge = accountFacade.findConfirmedFalse();
        int timeout = Integer.parseInt(applicationProperties.getProperty("unconfirmed.account.deletion.timeout.hours"));

        for (Account account : accountsToPurge) {
            LocalDateTime timeoutThreshold = LocalDateTime.now().minusHours(timeout);
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
