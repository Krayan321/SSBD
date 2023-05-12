package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CreatePatientDTO {

  AccountDTO accountDTO;

  PatientDataDTO patientDataDTO;

  @ToString.Exclude private String password;
}
