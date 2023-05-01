package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import java.util.Locale;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountAndAccessLevelsDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.RegisterPatientDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

public class AccountConverter {

    private AccountConverter() {
    }

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

    public static AdminData dtoToAdminData(AdminDataDTO adminDataDTO) {
        AdminData adminData = new AdminData();
        return adminData;
    }

    public static AdminDataDTO dtoFromAdminData() {
        return null;
    }

    public static Account mapAccountDtoToAccount(AccountDTO accountDTO, String password) {
        return Account.builder()
                .login(accountDTO.getLogin())
                .password(password)
                .build();
    }

    public static Account accountRegisterDtoToAccount(RegisterPatientDto registerPatientDto) {

        Account account = Account.builder()
                .login(registerPatientDto.getLogin())
                .password(registerPatientDto.getPassword())
                .build();
        account.setEmail(registerPatientDto.getEmail());
        account.setActive(false);
        account.setConfirmed(false);
        account.setLanguage(Locale.ENGLISH);

        PatientData patientData = PatientData.builder()
                .firstName(registerPatientDto.getName())
                .lastName(registerPatientDto.getLastName())
                .pesel(registerPatientDto.getPesel())
                .phoneNumber(registerPatientDto.getPhoneNumber())
                .NIP(registerPatientDto.getNIP())
                .build();

        patientData.setActive(false);
        account.getAccessLevels().add(patientData);
        patientData.setAccount(account);

        return account;
    }


}
