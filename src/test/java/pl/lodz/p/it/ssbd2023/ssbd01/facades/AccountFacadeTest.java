package pl.lodz.p.it.ssbd2023.ssbd01.facades;


import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

import static org.junit.jupiter.api.Assertions.*;

class AccountFacadeTest {
    @Test
    public void creationTest() {
        AccountFacade af = new AccountFacade();
        Account account = new Account("login", "password");
        account.setActive(true);
        account.setRegistered(true);

        af.create(account);
    }
}