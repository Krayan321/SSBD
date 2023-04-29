package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

public class AccountConverter {

    private AccountConverter() {
    }

    public static Account mapAccountDtoToAccount(AccountDTO accountDTO, String password) {
        return Account.builder()
                .login(accountDTO.getLogin())
                .password(password)
                .build();
    }




}
