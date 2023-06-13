package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class CategoryControllerIT extends BaseTest {

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
}
