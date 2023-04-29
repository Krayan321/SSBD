package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.*;

import java.util.HashSet;
import java.util.Set;

public class AccountConverter {

    public static Account dtoToAccount(AccountDTO accountDTO) {
        return null;
    }

    public static AccountDTO dtoFromAccount(Account account) {
        AccountDTO accountDTO = new AccountDTO();
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

    public static ChemistData dtoToChemistData() {
        return null;
    }

    public static ChemistDataDTO dtoFromChemistData() {
        return null;
    }

    public static AdminData dtoToAdminData() {
        return null;
    }

    public static AdminDataDTO dtoFromAdminData() {
        return null;
    }

}
