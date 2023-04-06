package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AccountFacadeLocal extends AbstractFacadeLocal<Account> {
    Account findByLogin(String login);
    List<Account> findAll();
}
