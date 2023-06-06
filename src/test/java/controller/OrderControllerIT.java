package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static controller.dataForTests.adminLoginDto;
import static controller.dataForTests.categoryDto;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT extends BaseTest {

    static String adminJwt;



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
}