package pl.lodz.p.it.ssbd2023.ssbd01.util;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;

public class AccessLevelFinder {
  public static AccessLevel findAccessLevel(Account account, Role role) {
    for (AccessLevel next : account.getAccessLevels()) {
      if (next.getRole().equals(role)) {
        return next;
      }
    }
    throw ApplicationException.createEntityNotFoundException();
  }
}
