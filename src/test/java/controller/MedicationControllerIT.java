package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MedicationControllerIT extends BaseTest {

    static String chemistJwt;

    @BeforeAll
    static void setUp() throws InterruptedException {
        System.out.println(getApiRoot());
        chemistJwt = given()
                .contentType("application/json")
                .body(chemistLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().jsonPath().getString("jwtToken");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }


    @Test
    @Order(1)
    public void addCategory() {
        given()
                .header("Authorization", "Bearer " + chemistJwt)
                .body(categoryDto)
                .post(getApiRoot() + "/category/add-category")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }
    @Test
    @Order(2)
    public void addMedication_correct() {
        given()
                .header("Authorization", "Bearer " + chemistJwt)
                .body(addMedicationDto)
                .post(getApiRoot() + "/medication/add-medication")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

//    @Test
//    @Order(3)
//    public void addMedication_sameName() {
//        given()
//                .header("Authorization", "Bearer " + adminJwt)
//                .body(addMedicationDto)
//                .post(getApiRoot() + "/medication/add-medication")
//                .then()
//                .statusCode(Response.Status.CONFLICT.getStatusCode());
//    }
//
//    @Test
//    @Order(4)
//    public void addMedication_noSuchCategory() {
//        given()
//                .header("Authorization", "Bearer " + adminJwt)
//                .body(addMedicationDto)
//                .post(getApiRoot() + "/medication/add-medication")
//                .then()
//                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
//    }

    @Test
    @Order(5)
    public void getMedications_correct() {
        given()
                .header("authorization", "Bearer " + chemistJwt)
                .get(getApiRoot() + "/account/")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasSize(equalTo(5)));
    }
}
