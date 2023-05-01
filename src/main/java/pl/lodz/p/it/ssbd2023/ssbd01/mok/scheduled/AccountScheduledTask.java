package pl.lodz.p.it.ssbd2023.ssbd01.mok.scheduled;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;

@Startup
@Singleton
public class AccountScheduledTask {

    @Inject
    AccountManagerLocal accountManager;

    @Schedule(hour = "*", minute = "0", second = "0", info = "Each hour")
    public void purgeUnactivatedAccounts() {
        accountManager.purgeUnactivatedAccounts();
    }
}
