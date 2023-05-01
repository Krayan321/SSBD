package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.HashSet;

public class AccountConverter {

    public static Account dtoToAccount(AccountAndAccessLevelsDTO accountDTO) {
        return null;
    }

    public static AccountDTO dtoFromAccount(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setVersion(accountDTO.getVersion());
        accountDTO.setLogin(account.getLogin());
        accountDTO.setActive(account.getActive());
        accountDTO.setConfirmed(account.getConfirmed());
        return accountDTO;
    }

    public static AccountAndAccessLevelsDTO dtoFromAccountAndAccessLevels(Account account) {
        AccountAndAccessLevelsDTO accountDTO = new AccountAndAccessLevelsDTO();
        accountDTO.setVersion(accountDTO.getVersion());
        accountDTO.setAccessLevels(
                AccessLevelConverter.mapAccessLevelsToAccessLevelsDto(account.getAccessLevels()));
        accountDTO.setLogin(account.getLogin());
        accountDTO.setActive(account.getActive());
        accountDTO.setConfirmed(account.getConfirmed());
        return accountDTO;
    }

    private AccountConverter() {
    }

    public static Account mapAccountDtoToAccount(AccountDTO accountDTO, String password) {
        return Account.builder()
                .login(accountDTO.getLogin())
                .password(password)
                .build();
    }




}
