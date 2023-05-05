package controller;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddAdminAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin.AddChemistAccountDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

public class dataForTests {

    public static LoginDTO adminLoginDto = new LoginDTO("admin123", "admin123");

    public static LoginDTO patientLoginDto = new LoginDTO("test1", "testPatient");

    public static RegisterPatientDTO registerPatientDtoDuplicateLogin =
            RegisterPatientDTO.builder()
                    .login(patientLoginDto.getLogin())
                    .password(patientLoginDto.getPassword())
                    .email("other-patient-email@local.db")
                    .name("Test")
                    .lastName("Patient")
                    .phoneNumber("123 123 123")
                    .pesel("012345678901")
                    .nip("444-333-22-11")
                    .build();

    public static RegisterPatientDTO registerPatientDtoDuplicateEmail =
            RegisterPatientDTO.builder()
                    .login("other-login")
                    .password(patientLoginDto.getPassword())
                    .email("patient-email@local.db")
                    .name("Test")
                    .lastName("Patient")
                    .phoneNumber("123 123 123")
                    .pesel("012345678901")
                    .nip("444-333-22-11")
                    .build();

    public static GrantChemistDataDTO grantChemistDataDTO =
            new GrantChemistDataDTO("1234");

    public static GrantAdminDataDTO grantAdminDataDTO = new GrantAdminDataDTO("1234");

    // todo create this from response
    public static ChemistDataDTO chemistDataDTOChangedLiscence = ChemistDataDTO.builder()
            .id(3L).version(0L).role(Role.CHEMIST)
            .active(false).licenseNumber("4123123123123")
            .build();

    public static AddChemistAccountDto addChemistAccountDto = AddChemistAccountDto.builder()
            .login("testChemist")
            .password("testChemist")
            .email("testChemist@o2.pl")
            .licenseNumber("123456789")
            .build();

    public static AddAdminAccountDto addAdminAccountDto = AddAdminAccountDto.builder()
            .login("testAdmin")
            .password("testAdmin")
            .email("testAdmin@o2.pl")
            .workPhoneNumber("123456789")
            .build();

    public static AddChemistAccountDto addChemistAccountDtoMissingField =
            AddChemistAccountDto.builder()
                    .login("incorrectChemist")
                    .password("incorrectChemist")
                    .email("incorrectChemist@o2.pl")
                    .build();

    public static AddAdminAccountDto addAdminAccountDtoMissingField = AddAdminAccountDto.builder()
            .login("incorrectAdmin")
            .password("incorrectAdmin")
            .email("incorrectAdmin@o2.pl")
            .build();


}
