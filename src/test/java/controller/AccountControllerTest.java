package controller;

import static controller.dataForTests.addAdminAccountDto;
import static controller.dataForTests.addAdminAccountDtoMissingField;
import static controller.dataForTests.addChemistAccountDto;
import static controller.dataForTests.addChemistAccountDtoMissingField;
import static controller.dataForTests.adminLoginDto;
import static controller.dataForTests.chemistDataDTOChangedLiscence;
import static controller.dataForTests.grantAdminDataDTO;
import static controller.dataForTests.grantChemistDataDTO;
import static controller.dataForTests.patientLoginDto;
import static controller.dataForTests.registerPatientDtoDuplicateEmail;
import static controller.dataForTests.registerPatientDtoDuplicateLogin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.RegisterPatientDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends BaseTest {
    static String adminJwt;

    static String patientJwt;
    private static RegisterPatientDTO registerPatientDto = RegisterPatientDTO.builder()
            .login(patientLoginDto.getLogin())
            .password(patientLoginDto.getPassword())
            .email("patient-email@local.db")
            .name("Test")
            .lastName("Patient")
            .phoneNumber("123 123 123")
            .pesel("012345678901")
            .nip("444-333-22-11")
            .build();
    PatientDataDTO patientDataDTOChangedName = PatientDataDTO.builder()
            .id(2L).version(0L).role(Role.PATIENT).active(false)
            .pesel(registerPatientDto.getPesel())
            .firstName("Othername")
            .lastName(registerPatientDto.getLastName())
            .phoneNumber(registerPatientDto.getPhoneNumber())
            .NIP(registerPatientDto.getNip())
            .build();

    @BeforeAll
    static void setUp() {
        System.out.println(getApiRoot());
        adminJwt = given()
                .contentType("application/json")
                .body(adminLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then()
                .log().all()

                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().asString();

        //globally set authorization header
        RestAssured.requestSpecification =
                new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .log(LogDetail.ALL)
//                        .addHeader("authorization", "Bearer " + adminJwt)
                        .build();
    }

    // todo add verification of internationalised message
    @Test
    @Order(1)
    public void login_invalidLogin() {
        LoginDTO invalidLogin = new LoginDTO("nonexistantuser", adminLoginDto.getPassword());

        given().body(invalidLogin)
                .post(getApiRoot() + "/auth/login")
                .then()
                .log().all()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(2)
    public void readAccount_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/account/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("login", equalTo(adminLoginDto.getLogin()));
    }

    @Test
    @Order(3)
    public void readAccount_noSuchUser() {
        given().header("authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/account/2")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(4)
    public void registerPatient_correct() {
        given().body(registerPatientDto)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(5)
    public void registerPatient_duplicateLogin() {
        given().body(registerPatientDtoDuplicateLogin)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

    @Test
    @Order(6)
    public void registerPatient_duplicateEmail() {
        given().body(registerPatientDtoDuplicateEmail)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

    @Test
    @Order(7)
    public void login_correct() {
        patientJwt = given().body(patientLoginDto)
                .post(getApiRoot() + "/auth/login")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().asString();
    }

    // access level id: 3
    @Test
    @Order(8)
    public void grantChemist_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(grantChemistDataDTO)
                .put(getApiRoot() + "/account/2/grantChemist")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    // access level id: 4
    @Test
    @Order(9)
    public void grantAdmin_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(grantAdminDataDTO)
                .put(getApiRoot() + "/account/2/grantAdmin")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(10)
    public void readAccountAndAccessLevels_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/account/2/details")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("accessLevels", hasSize(3));
    }

    @Test
    @Order(11)
    public void editChemistData_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(chemistDataDTOChangedLiscence)
                .put(getApiRoot() + "/account/2/chemist")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("accessLevels", hasItem(hasEntry("licenseNumber",
                        chemistDataDTOChangedLiscence.getLicenseNumber())));
    }

    @Test
    @Order(11)
    public void editPatientData_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(patientDataDTOChangedName)
                .put(getApiRoot() + "/account/2/patient")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("accessLevels", hasItem(hasEntry("firstName",
                        patientDataDTOChangedName.getFirstName())));
    }


    // todo admin? for now it changes nothing
    @Test
    @Order(12)
    public void addChemist_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addChemistAccountDto)
                .post(getApiRoot() + "/account/add-chemist")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(13)
    public void addChemist_incorrect_duplicate() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addChemistAccountDto)
                .post(getApiRoot() + "/account/add-chemist")
                .then()
                .log().all()
                .statusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

    @Test
    @Order(14)
    public void addChemist_incorrect_missing_field() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addChemistAccountDtoMissingField)
                .post(getApiRoot() + "/account/add-chemist")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(15)
    public void addAdmin_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addAdminAccountDto)
                .post(getApiRoot() + "/account/add-admin")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(16)
    public void addAdmin_incorrect_duplicate() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addAdminAccountDto)
                .post(getApiRoot() + "/account/add-admin")
                .then()
                .log().all()
                .statusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

    @Test
    @Order(17)
    public void addAdmin_incorrect_missing_field() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(addAdminAccountDtoMissingField)
                .post(getApiRoot() + "/account/add-admin")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

}