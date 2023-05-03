package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import java.util.Locale;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountAndAccessLevelsDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.RegisterPatientDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

public class AccountConverter {

    private AccountConverter() {
    }

    public static AccountDTO mapAccountToAccountDto(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .version(account.getVersion())
                .confirmed(account.getConfirmed())
                .active(account.getActive())
                .login(account.getLogin())
                .build();
    }

    public static AccountAndAccessLevelsDTO mapAccountToAccountAndAccessLevelsDto(Account account) {
        var accessLevels = AccessLevelConverter.mapAccessLevelsToAccessLevelsDto(account.getAccessLevels());
        return AccountAndAccessLevelsDTO.builder()
                .id(account.getId())
                .version(account.getVersion())
                .accessLevels(accessLevels)
                .login(account.getLogin())
                .active(account.getActive())
                .confirmed(account.getConfirmed())
                .build();
    }

    public static Account mapRegisterPatientDtoToAccount(RegisterPatientDto registerPatientDto) {

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
                .NIP(registerPatientDto.getNip())
                .build();

        patientData.setActive(false);
        account.getAccessLevels().add(patientData);
        patientData.setAccount(account);

        return account;
    }


}
