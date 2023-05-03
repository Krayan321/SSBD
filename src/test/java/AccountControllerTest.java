import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.CreatePatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.LoginDto;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.RegisterPatientDto;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTest extends BaseTest {
    static String adminJwt;

    private static LoginDto adminLoginDto = new LoginDto("admin123", "admin123");

    private static LoginDto patientLoginDto = new LoginDto("test1", "testPatient");

    private static CreatePatientDataDTO createPatientDataDTO = new CreatePatientDataDTO(
            "012345678901", "Test", "Patient Dwa",
            "011 213 213", "231-132-32-23");

    private static RegisterPatientDto registerPatientDto = RegisterPatientDto.builder()
            .login(patientLoginDto.getLogin())
            .password(patientLoginDto.getPassword())
            .email("patient-email@local.db")
            .name("Test")
            .lastName("Patient")
            .phoneNumber("123 123 123")
            .pesel("012345678901")
            .nip("444-333-22-11")
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
    public void login_invalidLogin() {
        LoginDto invalidLogin = new LoginDto("nonexistantuser", adminLoginDto.getPassword());

        given().body(invalidLogin)
                .post(getApiRoot() + "/auth/login")
                .then()
                .log().all()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void registerPatient_correct() {
        given().body(registerPatientDto)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void registerPatient_duplicateLogin() {
        given().body(registerPatientDto)
                .post(getApiRoot() + "/account/register")
                .then()
                .log().all()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

}
