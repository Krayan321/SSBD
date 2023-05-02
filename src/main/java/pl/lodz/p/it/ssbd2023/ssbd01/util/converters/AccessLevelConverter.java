package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AccessLevelConverter {

    private AccessLevelConverter() {
    }

    public static PatientData mapPatientDataDtoToPatientData(PatientDataDTO patientDataDTO) {

        return PatientData.builder()
                .firstName(patientDataDTO.getFirstName())
                .lastName(patientDataDTO.getLastName())
                .NIP(patientDataDTO.getNIP())
                .phoneNumber(patientDataDTO.getPhoneNumber())
                .pesel(patientDataDTO.getPesel())
                .build();
    }

    public static Set<AccessLevelDTO> mapAccessLevelsToAccessLevelsDto(Set<AccessLevel> levels) {
        return null == levels ? null : levels.stream()
                .filter(Objects::nonNull)
                .map(AccessLevelConverter::mapAccessLevelToAccessLevelDto)
                .collect(Collectors.toSet());
    }

    public static AccessLevelDTO mapAccessLevelToAccessLevelDto(AccessLevel accessLevel) {
        if(accessLevel instanceof PatientData)
            return mapPatientDataToPatientDataDto((PatientData) accessLevel);
        if(accessLevel instanceof ChemistData)
            return mapChemistDataToChemistDataDto((ChemistData) accessLevel);
        if(accessLevel instanceof AdminData)
            return mapAdminDataToAdminDataDto((AdminData) accessLevel);
        return null;
    }

    // PATIENT
    public static PatientDataDTO mapPatientDataToPatientDataDto(PatientData patientData) {
        return PatientDataDTO.builder()
                .id(patientData.getId())
                .version(patientData.getVersion())
                .role(patientData.getRole())
                .active(patientData.getActive())
                .pesel(patientData.getPesel())
                .firstName(patientData.getFirstName())
                .lastName(patientData.getLastName())
                .phoneNumber(patientData.getPhoneNumber())
                .NIP(patientData.getNIP())
                .build();
    }

    // todo setting role
    // todo builder
    public static PatientData dtoToPatientData(PatientDataDTO patientDataDTO) {
        PatientData patientData = new PatientData();
        patientData.setPesel(patientDataDTO.getPesel());
        patientData.setFirstName(patientDataDTO.getFirstName());
        patientData.setLastName(patientDataDTO.getLastName());
        patientData.setPhoneNumber(patientDataDTO.getPhoneNumber());
        patientData.setNIP(patientDataDTO.getNIP());
        return patientData;
    }

    public static PatientData mapCreatePatientDataDTOtoPatientData(CreatePatientDataDTO data) {
        return PatientData.builder()
                .pesel(data.getPesel())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .phoneNumber(data.getPhoneNumber())
                .NIP(data.getNIP())
                .build();
    }

    // CHEMIST
    public static ChemistDataDTO mapChemistDataToChemistDataDto(ChemistData chemistData) {
        return ChemistDataDTO.builder()
                .id(chemistData.getId())
                .version(chemistData.getVersion())
                .role(chemistData.getRole())
                .active(chemistData.getActive())
                .licenseNumber(chemistData.getLicenseNumber())
                .build();
    }

    public static ChemistData mapChemistDataDtoToChemistData(ChemistDataDTO chemistDataDTO) {
        return ChemistData.builder()
                .licenseNumber(chemistDataDTO.getLicenseNumber())
                .build();
    }

    public static ChemistData mapCreateChemistDataDtoToChemistData(CreateChemistDataDTO chemistData) {
        return ChemistData.builder()
                .licenseNumber(chemistData.getLicenseNumber())
                .build();
    }

    // ADMIN
    public static AdminDataDTO mapAdminDataToAdminDataDto(AdminData adminData) {
        return AdminDataDTO.builder()
                .id(adminData.getId())
                .version(adminData.getVersion())
                .role(adminData.getRole())
                .active(adminData.getActive())
                .build();
    }

    public static AdminData mapCreateAdminDataDtoToAdminData(CreateAdminDataDTO createAdminDataDTO) {
        return new AdminData();
    }

    public static AdminData dtoToAdminData(AdminDataDTO adminDataDTO) {
        return new AdminData();
    }
}
