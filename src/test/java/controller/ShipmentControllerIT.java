package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.MedicationCreateShipmentDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static controller.dataForTests.chemistLoginDto;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class ShipmentControllerIT extends BaseTest {

    static String chemistJwt;

    @BeforeAll
    static void setUp() {
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

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateShipment {

        static List<CreateShipmentMedicationDTO> medications =
                new ArrayList<>();
        static List<CreateShipmentMedicationDTO> medicationsNotExists =
                new ArrayList<>();
        static CreateShipmentDTO createShipmentDTO;

        @BeforeAll
        static void setUp() {
            var responseMed1 = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/1")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String etag1 = responseMed1.getHeader("ETag").replace("\"", "");
            Long version1 = responseMed1.getBody().jsonPath().getLong("version");
            String price1 = responseMed1.getBody().jsonPath().getString("currentPrice");
            String name1 = responseMed1.getBody().jsonPath().getString("name");

            medications.add(CreateShipmentMedicationDTO.builder()
                    .quantity(2)
                    .medication(MedicationCreateShipmentDTO.builder()
                            .name(name1)
                            .version(version1)
                            .etag(etag1)
                            .price(BigDecimal.valueOf(Double.parseDouble(price1)))
                            .build())
                    .build());

            var responseMed2 = given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/medication/2")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .extract().response();
            String etag2 = responseMed2.getHeader("ETag").replace("\"", "");
            Long version2 = responseMed2.getBody().jsonPath().getLong("version");
            String price2 = responseMed2.getBody().jsonPath().getString("currentPrice");
            String name2 = responseMed2.getBody().jsonPath().getString("name");

            medications.add(CreateShipmentMedicationDTO.builder()
                    .quantity(5)
                    .medication(MedicationCreateShipmentDTO.builder()
                            .name(name2)
                            .version(version2)
                            .etag(etag2)
                            .price(BigDecimal.valueOf(Double.parseDouble(price2)))
                            .build())
                    .build());

            medicationsNotExists.add(CreateShipmentMedicationDTO.builder()
                    .quantity(5)
//                    .medication(new MedicationCreateShipmentDTO(999L))
                    .build());

            createShipmentDTO = CreateShipmentDTO.builder()
                    .shipmentDate(LocalDateTime.now().toString())
                    .shipmentMedications(medications)
                    .build();
        }

        @Test
        @Order(1)
        public void createShipment_correct() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(2)
        public void createShipment_medicationNotExist() {
            createShipmentDTO.setShipmentMedications(medicationsNotExists);
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
            createShipmentDTO.setShipmentMedications(medications);
        }

        @Test
        @Order(3)
        public void createShipment_invalidDate() {
            createShipmentDTO.setShipmentDate("not a date");
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                    .body("message", equalTo(EXCEPTION_INCORRECT_DATE_FORMAT));
            createShipmentDTO.setShipmentDate(LocalDateTime.now().toString());
        }

        @Test
        @Order(4)
        public void createShipment_noMedications() {
            createShipmentDTO.setShipmentMedications(new ArrayList<>());
            given().header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
            createShipmentDTO.setShipmentMedications(medications);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReadShipment {
        @Test
        @Order(1)
        public void getShipment_correct() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment/1")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        public void getShipment_notFound() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment/999")
                    .then().log().all()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                    .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
        }

        @Test
        @Order(3)
        public void getAllShipments_correct() {
            given().header("authorization", "Bearer " + chemistJwt)
                    .get(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.OK.getStatusCode());
        }
    }
}
