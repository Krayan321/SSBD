package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.order.CreateOrderMedicationDTO;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Testcontainers
public class OrderControllerIT extends BaseTest {

    @AfterAll
    static void end() {
        afterAll();
    }
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
    class GetOrdersToApprove {
        @Test
        @Order(1)
        public void createOrderMedication_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .get(getApiRoot() + "/order/to-approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ApproveOrder {
        @Test
        @Order(1)
        public void approveOrder_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void approveOrder_incorrectStatus() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/approve")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION));
        }

        @Test
        @Order(3)
        public void approveOrder_notFound() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/999/approve")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ENTITY_NOT_FOUND));
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
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CancelOrder {
        @Test
        @Order(1)
        public void cancelOrder_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/5/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void cancelOrder_incorrectStatus() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/4/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_ILLEGAL_STATE_MODIFICATION));
        }

        @Test
        @Order(3)
        public void cancelOrder_notFound() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/999/cancel")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ENTITY_NOT_FOUND));
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteWaitingOrderById {
        @Test
        @Order(1)
        public void deleteWaitingOrderById_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/1/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void deleteWaitingOrderById_not_in_queue() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/1/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetOrder {
//        @Test
//        @Order(1)
//        public void getAllOrders_correct() {
//            given().header("Authorization", "Bearer " + chemistJwt)
//                    .log().all()
//                    .get(getApiRoot() + "/order/")
//                    .then().log().all()
//                    .statusCode(Response.Status.OK.getStatusCode());
//        }
        @Test
        @Order(2)
        public void getWaitingOrders_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .get(getApiRoot() + "/order/waiting")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
        @Test
        @Order(3)
        public void updateQueue_correct() {
            given().header("Authorization", "Bearer " + chemistJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/update-queue")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class WithdrawOrderById {
        @Test
        @Order(1)
        public void withdrawOrderById_bad_state() {
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/1/withdraw")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode());
        }
        @Test
        @Order(2)
        public void withdrawOrderById_correct() {
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/6/withdraw")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }

    @Nested
    @Order(7)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class patientApprovedById {
        @Test
        @Order(1)
        public void patientApprovedById_bad_State(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/2/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_NO_PERMISSION_TO_APPROVE_ORDER));
        }

        @Test
        @Order(2)
        public void patientApprovedById_not_found(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/500/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                    .body("message", CoreMatchers.equalTo(EXCEPTION_ORDER_NOT_FOUND));
        }

        @Test
        @Order(3)
        public void patientApprovedById_correct(){
            given().header("Authorization", "Bearer " + patientJwt)
                    .log().all()
                    .put(getApiRoot() + "/order/7/patient-approve")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }
}
