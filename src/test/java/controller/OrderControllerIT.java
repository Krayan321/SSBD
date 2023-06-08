package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT extends BaseTest {

    static String adminJwt;
    static String patientJwt;


    @BeforeAll
    static void setUp() throws InterruptedException {
        System.out.println(getApiRoot());
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

        RestAssured.requestSpecification =
                new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .log(LogDetail.ALL)
                        .build();

        String jsonPatientJwt =
                given()
                        .contentType("application/json")
                        .body(patient2LoginDto)
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

        patientJwt = jsonPatientJwt.substring(jsonPatientJwt.indexOf(":") + 2, jsonPatientJwt.length() - 2);
    }


    @Test
    @Order(1)
    public void cannot_read_self_orders_when_role_is_not_patient() {
        given()
                .header("Authorization", "Bearer " + adminJwt)
                .get(getApiRoot() + "/order/self")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    @Order(2)
    void should_successfully_read_own_orders_as_patient() {
        given()
                .header("Authorization", "Bearer " + patientJwt)
                .get(getApiRoot() + "/order/self")
                .then()
                .log().all()
                .body("[0].inQueue", equalTo(true))
                .body("[0].orderMedication[0].medication.name", equalTo("Prozac"))
                .body("[0].orderMedication[0].medication.price", equalTo(10.00f))
                .body("[0].orderMedication[0].quantity", equalTo(2))
                .body("[0].orderMedication[1].medication.name", equalTo("Zoloft"))
                .body("[0].orderMedication[1].medication.price", equalTo(20.00f))
                .body("[0].orderMedication[1].quantity", equalTo(4))
                .body("[0].prescription.prescriptionNumber", equalTo("123456789"))
                .body("[1].inQueue", equalTo(true))
                .body("[1].orderMedication[0].medication.name", equalTo("Marsjanki"))
                .body("[1].orderMedication[0].medication.price", equalTo(25.00f))
                .body("[1].orderMedication[0].quantity", equalTo(20))
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
