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
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT extends BaseTest {

    static String patientJwt;
    static String chemistJwt;
    static String adminJwt;


    @BeforeAll
    static void setUp() throws InterruptedException {
        System.out.println(getApiRoot());
        patientJwt = given()
                .contentType("application/json")
                .body(patientLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");
        chemistJwt = given()
                .contentType("application/json")
                .body(chemistLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");
        adminJwt = given()
                .contentType("application/json")
                .body(adminLoginDto)
                .log().all()
                .post(getApiRoot() + "/auth/login")
                .then().log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().response().jsonPath().getString("jwtToken");

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

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetOrdersToApprove {
        @Test
        @Order(1)
        public void createOrderMedication_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .get(getApiRoot() + "/order/to-approve")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
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
