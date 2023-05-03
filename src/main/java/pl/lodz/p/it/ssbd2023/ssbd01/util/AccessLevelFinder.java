package pl.lodz.p.it.ssbd2023.ssbd01.util;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

import java.util.Iterator;

public class AccessLevelFinder {
    public static AccessLevel findAccessLevel(Account account, AccessLevel accessLevel) {
        for (AccessLevel next : account.getAccessLevels()) {
            if (next.equals(accessLevel))
                return next;
        }
        ApplicationException.createEntityNotFoundException();
        return null;
    }
}
