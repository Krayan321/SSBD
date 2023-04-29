package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;

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

}
