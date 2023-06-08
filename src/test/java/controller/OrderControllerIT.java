package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT extends BaseTest {

    static String patientJwt;
    static String adminJwt;

    @BeforeAll
    static void setUp() throws InterruptedException {
        System.out.println(getApiRoot());
        EditAccountDTO editAccountDTO;
        String etag;



        String jsonJwt =
                given()
                        .contentType("application/json")
                        .body(adminLoginDto)
                        .log()
                        .all()
                        .post(getApiRoot() + "/auth/login")
                        .then()
                        .log()
                        .all()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .response()
                        .asString();
        adminJwt = jsonJwt.substring(jsonJwt.indexOf(":") + 2, jsonJwt.length() - 2);

        given()
                .contentType("application/json")
                .body(registerPatientDto)
                .post(getApiRoot() + "/account/register")
                .then()
                .log()
                .all()
                .statusCode(Response.Status.CREATED.getStatusCode());

        var response = given()
                .header("authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/account/3")
                .then()
                .log()
                .all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .response();

        etag = response.getHeader("ETag").replace("\"", "");
        Long version = response.getBody().jsonPath().getLong("version");
        editAccountDTO = EditAccountDTO.builder()
                .version(version)
                .login(patientLoginDto.getLogin())
                .email("patient1@mail.pl")
                .build();

        given()
                .contentType("application/json")
                .header("authorization", "Bearer " + adminJwt).header("If-Match", etag)
                .body(editAccountDTO)
                .put(getApiRoot() + "/account/3")
                .then()
                .log()
                .all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("email", equalTo("patient1@mail.pl"),"confirmed", equalTo(true));

        jsonJwt =
                given()
                        .contentType("application/json")
                        .body(patientLoginDto)
                        .log()
                        .all()
                        .post(getApiRoot() + "/auth/login")
                        .then()
                        .log()
                        .all()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .extract()
                        .response()
                        .asString();

        patientJwt = jsonJwt.substring(jsonJwt.indexOf(":") + 2, jsonJwt.length() - 2);

        RestAssured.requestSpecification =
                new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .log(LogDetail.ALL)
                        .build();
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateOrderMedication {
        @Test
        @Order(1)
        public void createOrderMedication_correct() {
            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .log()
                    .all()
                    .body(createOrderMedicationDTO)
                    .put(getApiRoot() + "/order/1/add")
                    .then()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(2)
        public void createOrderMedication_medication_not_found() {
            CreateOrderMedicationDTO dto = createOrderMedicationDTO;
            dto.setMedicationDTOId(5000L);
            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .log()
                    .all()
                    .body(dto)
                    .put(getApiRoot() + "/order/1/add")
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }

        @Test
        @Order(3)
        public void createOrderMedication_order_not_found() {
            given()
                    .header("Authorization", "Bearer " + patientJwt)
                    .log()
                    .all()
                    .body(createOrderMedicationDTO)
                    .put(getApiRoot() + "/order/5000/add")
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}

