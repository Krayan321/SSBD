package controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.CreateShipmentDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.shipment.ShipmentMedicationDTO;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static controller.dataForTests.chemistLoginDto;
import static io.restassured.RestAssured.given;

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

        static List<ShipmentMedicationDTO> shipmentMedicationDTOs = new ArrayList<>();
        static CreateShipmentDTO createShipmentDTO;

        @BeforeAll
        static void setUp() {
            shipmentMedicationDTOs.add(ShipmentMedicationDTO.builder()
                    .quantity(2)
                    .medication(null)
                    .build());

            shipmentMedicationDTOs.add(ShipmentMedicationDTO.builder()
                    .quantity(5)
                    .medication(null)
                    .build());

            createShipmentDTO = CreateShipmentDTO.builder()
                    .shipmentDate(Date.from(Instant.now()))
                    .shipmentMedications(shipmentMedicationDTOs)
                    .build();
        }

        @Test
        @Order(1)
        public void createShipment_correct() {
            given()
                    .header("authorization", "Bearer " + chemistJwt)
                    .body(createShipmentDTO)
                    .post(getApiRoot() + "/shipment")
                    .then().log().all()
                    .statusCode(Response.Status.CREATED.getStatusCode());
            // todo
        }
    }
}
