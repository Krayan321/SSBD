import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends BaseTest {
    static String adminJwt;

    static String patientJwt;

    private static LoginDTO adminLoginDto = new LoginDTO("admin123", "admin123");

    private static LoginDTO patientLoginDto = new LoginDTO("test1", "testPatient");

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

    @Test
    @Order(4)
    public void registerPatient_correct() {
        given().body(registerPatientDto)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    private static RegisterPatientDTO registerPatientDtoDuplicateLogin =
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

    @Test
    @Order(5)
    public void registerPatient_duplicateLogin() {
        given().body(registerPatientDtoDuplicateLogin)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.EXPECTATION_FAILED.getStatusCode());
    }

    private static RegisterPatientDTO registerPatientDtoDuplicateEmail =
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

    private static CreateChemistDataDTO createChemistDataDTO =
            new CreateChemistDataDTO("1234");


    @Test
    @Order(8)
    public void grantChemist_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(createChemistDataDTO)
                .put(getApiRoot() + "/account/2/grantChemist")
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    private static CreateAdminDataDTO createAdminDataDTO = new CreateAdminDataDTO();

    @Test
    @Order(9)
    public void grantAdmin_correct() {
        given().header("authorization", "Bearer " + adminJwt)
                .body(createAdminDataDTO)
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


}
