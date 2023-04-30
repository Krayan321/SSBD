package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import java.util.HashSet;
import java.util.Locale;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountAndAccessLevelsDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.RegisterPatientDto;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

public class AccountConverter {

    private AccountConverter() {
    }

    public static Account dtoToAccount(AccountAndAccessLevelsDTO accountDTO) {
        return null;
    }

    public static AccountAndAccessLevelsDTO dtoFromAccount(Account account) {
        AccountAndAccessLevelsDTO accountDTO = new AccountAndAccessLevelsDTO();
        accountDTO.setVersion(accountDTO.getVersion());
        accountDTO.setAccessLevels(new HashSet<>());
        accountDTO.setLogin(account.getLogin());
        accountDTO.setActive(account.getActive());
        accountDTO.setConfirmed(account.getConfirmed());
        return null;
    }

    public static PatientData dtoToPatientData(PatientDataDTO patientDataDTO) {
        PatientData patientData = new PatientData();
        patientData.setPesel(patientDataDTO.getPesel());
        patientData.setFirstName(patientDataDTO.getFirstName());
        patientData.setLastName(patientDataDTO.getLastName());
        patientData.setPhoneNumber(patientDataDTO.getPhoneNumber());
        patientData.setNIP(patientDataDTO.getNIP());
        return patientData;
    }

    public static PatientDataDTO dtoFromPatientData() {
        return null;
    }

    public static ChemistData dtoToChemistData(ChemistDataDTO chemistDataDTO) {
        ChemistData chemistData = new ChemistData();
        chemistData.setLicenseNumber(chemistDataDTO.getLicenseNumber());
        return chemistData;
    }

    public static ChemistDataDTO dtoFromChemistData() {
        return null;
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
