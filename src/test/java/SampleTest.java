import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.LoginDto;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleTest extends BaseTest{

    static String jwt;

    private static LoginDto loginDto = new LoginDto("admin123", "admin123");


    @BeforeAll
   static void setUp() {


        jwt = given()
                .contentType("application/json")
                .body(loginDto)
                .log().all()
                .post(String.format("http://%s:%s/api/auth/login",
                        microContainer.getHost(), microContainer.getMappedPort(8080)))
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
                        .addHeader("authorization", "Bearer " + jwt)
                        .build();

    }

    @Test
    void test() {
        given()
                .when()
                .get(String.format("http://%s:%s/api/account",
                        microContainer.getHost(), microContainer.getMappedPort(8080)))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .log().all()
                .extract().response().prettyPrint();
    }

}
