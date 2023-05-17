package controller;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditPatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;

public class dataForTests {

  public static LoginDTO adminLoginDto = new LoginDTO("admin123", "P@ssw0rd");
  public static LoginDTO changedLoginDto = new LoginDTO("admin123", "testAdm1n!23");
  public static LoginDTO patientLoginDto = new LoginDTO("test11", "testP@tient123");
  public static LoginDTO grantAdminToChemistLogin = new LoginDTO("chemist123", "P4$$w0Rd");

  // register
  public static RegisterPatientDTO registerPatientDto =
      RegisterPatientDTO.builder()
          .login(patientLoginDto.getLogin())
          .password(patientLoginDto.getPassword())
          .email("patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateLogin =
      RegisterPatientDTO.builder()
          .login(patientLoginDto.getLogin())
          .password(patientLoginDto.getPassword())
          .email("other-patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateEmail =
      RegisterPatientDTO.builder()
          .login("other-login")
          .password(patientLoginDto.getPassword())
          .email("patient-email@local.db")
          .name("Test")
          .lastName("Patient")
          .phoneNumber("123123123")
          .pesel("12345678901")
          .nip("4443332211")
          .build();

  public static RegisterPatientDTO registerPatientDtoDuplicatePesel =
        RegisterPatientDTO.builder()
            .login("pesel-login")
            .password(patientLoginDto.getPassword())
            .email("pesel-patient-email@local.db")
            .name("Test")
            .lastName("Patient")
                .phoneNumber("123123321")
                .pesel("12345678901")
                .nip("4443332213")
                .build();

  public static RegisterPatientDTO registerPatientDtoDuplicateNip =
        RegisterPatientDTO.builder()
            .login("nip-login")
            .password(patientLoginDto.getPassword())
            .email("nip-patient-email@local.db")
            .name("Test")
            .lastName("Patient")
                .phoneNumber("123123321")
                .pesel("12345678999")
                .nip("4443332211")
                .build();

    public static RegisterPatientDTO registerPatientDtoDuplicatePhoneNumber =
        RegisterPatientDTO.builder()
            .login("phone-login")
            .password(patientLoginDto.getPassword())
            .email("phone-patient-email@local.db")
            .name("Test")
            .lastName("Patient")
                .phoneNumber("123123123")
                .pesel("12345678910")
                .nip("1443332211")
                .build();
  // grant
  public static GrantChemistDataDTO grantChemistDataDTO = new GrantChemistDataDTO("123412");

  public static GrantAdminDataDTO grantAdminDataDTO = new GrantAdminDataDTO("999888777");

  // edit data
  public static EditAdminDataDTO adminDataDTOChangedPhone =
      EditAdminDataDTO.builder().id(1L).version(0L).workPhoneNumber("102938129").build();

  public static EditChemistDataDTO chemistDataDTOChangedLiscence =
      EditChemistDataDTO.builder().id(4L).version(0L).licenseNumber("412312").build();

  public static EditPatientDataDTO patientDataDTOChangedName =
      EditPatientDataDTO.builder()
          .id(3L)
          .version(0L)
          .pesel(registerPatientDto.getPesel())
          .firstName("Othername")
          .lastName(registerPatientDto.getLastName())
          .phoneNumber(registerPatientDto.getPhoneNumber())
          .nip(registerPatientDto.getNip())
          .build();

  // add account
  public static AddChemistAccountDto addChemistAccountDto =
      AddChemistAccountDto.builder()
          .login("testChemist")
          .password("testCh3m!st")
          .email("testChemist@o2.pl")
          .licenseNumber("123456")
          .build();

  public static AddAdminAccountDto addAdminAccountDto =
      AddAdminAccountDto.builder()
          .login("testAdmin")
          .password("test@Dm1n")
          .email("testAdmin@o2.pl")
          .workPhoneNumber("123426123")
          .build();

  public static AddChemistAccountDto addChemistAccountDtoMissingField =
      AddChemistAccountDto.builder()
          .login("incorrectChemist")
          .password("incorrectCh3m!st")
          .email("incorrectChemist@o2.pl")
          .build();

  public static AddAdminAccountDto addAdminAccountDtoMissingField =
      AddAdminAccountDto.builder()
          .login("incorrectAdmin")
          .password("incorrect@Dm1n")
          .email("incorrectAdmin@o2.pl")
          .build();

  public static EditAccountDTO editEmailDto = new EditAccountDTO("new@email.local");
}
