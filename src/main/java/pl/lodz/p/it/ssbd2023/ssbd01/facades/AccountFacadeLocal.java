package pl.lodz.p.it.ssbd2023.ssbd01.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AccountFacadeLocal extends AbstractFacadeLocal<Account> {
    Account findByLogin(String login);
    List<Account> findAll();
}
