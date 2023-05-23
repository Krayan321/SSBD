package controller;

import static controller.dataForTests.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.ChangePasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.UpdateOtherUserPasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount.grant.GrantAdminDataDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountControllerIT extends BaseTest {

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
  }

  // todo test użycia jednego etagu z inną encją
  // pomyśleć jakie różne akcje mogą się przeplatać
  // sprawdzić czy przechodzą testy na remocie

  @Nested
  @Order(1)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class ChangeOwnPassword {
    private ChangePasswordDTO changePasswordDTO;
    private String etag;

    @BeforeEach
    public void init() {
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      changePasswordDTO = ChangePasswordDTO.builder()
              .login(adminLoginDto.getLogin())
              .version(version)
              .newPassword("!Admin321")
              .oldPassword(adminLoginDto.getPassword())
              .build();
    }

    @Test
    @Order(1)
    public void changeOwnPassword_same_as_old() {
      changePasswordDTO.setNewPassword(changePasswordDTO.getOldPassword());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_PASSWORD_NOT_CHANGED));
    }

    @Test
    @Order(2)
    public void changeOwnPassword_incorrect() {
      changePasswordDTO.setOldPassword("baD!password0");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
              .body("message", equalTo(EXCEPTION_AUTH_BAD_CREDENTIALS));
    }

    @Test
    @Order(3)
    public void changeOwnPassword_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      adminLoginDto.setPassword(changePasswordDTO.getNewPassword());
    }

    @Test
    @Order(4)
    public void changeOwnPassword_payloadMismatch() {
      String chemistJwt = given()
              .contentType("application/json")
              .body(chemistLoginDto)
              .log()
              .all()
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().response().jsonPath().getString("jwtToken");

      given()
              .header("authorization", "Bearer " + chemistJwt)
              .header("If-Match", etag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_MISMATCHED_PAYLOAD));
    }
  }

  @Nested
  @Order(2)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class ChangeUserPassword {
    private UpdateOtherUserPasswordDTO updateOtherUserPasswordDTO;
    private String etag;

    @BeforeEach
    public void init() {
      chemistJwt = given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().jsonPath().getString("jwtToken");
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/2")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      updateOtherUserPasswordDTO = UpdateOtherUserPasswordDTO.builder()
              .login(chemistLoginDto.getLogin())
              .version(version)
              .password("testChemist!23")
              .build();
    }

    @Test
    @Order(1)
    public void changeUserPassword_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(updateOtherUserPasswordDTO)
              .put(getApiRoot() + "/account/2/change-user-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());

      given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
              .body("message", equalTo(EXCEPTION_AUTH_BAD_CREDENTIALS));

      chemistLoginDto.setPassword(updateOtherUserPasswordDTO.getPassword());
      given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    public void changeUserPassword_noSuchUser() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(updateOtherUserPasswordDTO)
              .put(getApiRoot() + "/account/1000/change-user-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
    }

    private static ChangePasswordDTO changePasswordDTO;
    private static String ownChemistEtag;

    private static String chemistJwt;

    private void initUserOwn() {
      var response = given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      ownChemistEtag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      changePasswordDTO = ChangePasswordDTO.builder()
              .login(chemistLoginDto.getLogin())
              .version(version)
              .newPassword("31827931Aaa1@!")
              .oldPassword(chemistLoginDto.getPassword())
              .build();


    }

    @Test
    @Order(3)
    public void changeUserPassword_changeOwnPassword_conflict() {
      initUserOwn();
      String newPassword = "18237967183BBbb@!";
      updateOtherUserPasswordDTO.setPassword(newPassword);

      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(updateOtherUserPasswordDTO)
              .put(getApiRoot() + "/account/2/change-user-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());

      given()
              .header("authorization", "Bearer " + chemistJwt)
              .header("If-Match", ownChemistEtag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
      chemistLoginDto.setPassword(newPassword);

      given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("jwtToken", not(empty()));
    }

    @Test
    @Order(4)
    public void changeOwnPassword_changeUserPassword_conflict() {
      initUserOwn();
      String newPassword = "14697231Cc@!";
      updateOtherUserPasswordDTO.setPassword(newPassword);

      System.out.println(changePasswordDTO.toString());

      given()
              .header("authorization", "Bearer " + chemistJwt)
              .header("If-Match", ownChemistEtag)
              .body(changePasswordDTO)
              .put(getApiRoot() + "/account/change-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      chemistLoginDto.setPassword(changePasswordDTO.getNewPassword());

      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(updateOtherUserPasswordDTO)
              .put(getApiRoot() + "/account/2/change-user-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
    }

    @Test
    @Order(5)
    public void changeUserPassword_mismatchedPayload() {
      updateOtherUserPasswordDTO.setPassword("917SasdA!!");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(updateOtherUserPasswordDTO)
              .put(getApiRoot() + "/account/3/change-user-password")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_MISMATCHED_PAYLOAD));
    }
  }

  @Nested
  @Order(2)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class RegisterPatient {
    @Test
    @Order(1)
    public void registerPatient_correct() {
      given()
              .body(registerPatientDto)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    public void registerPatient_duplicateLogin() {
      given()
              .body(registerPatientDtoDuplicateLogin)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_LOGIN));
    }

    @Test
    @Order(3)
    public void registerPatient_duplicateEmail() {
      given()
              .body(registerPatientDtoDuplicateEmail)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_EMAIL));
    }

    @Test
    @Order(4)
    public void registerPatient_duplicatePhoneNumber() {
      given()
              .body(registerPatientDtoDuplicatePhoneNumber)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_PHONE_NUMBER));
    }

    @Test
    @Order(5)
    public void registerPatient_duplicatePesel() {
      given()
              .body(registerPatientDtoDuplicatePesel)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_PESEL));
    }

    @Test
    @Order(6)
    public void registerPatient_duplicateNip() {
      given()
              .body(registerPatientDtoDuplicateNip)
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_NIP));
    }

    @Test
    @Order(7)
    public void registerPatient_noBody() {
      given()
              .post(getApiRoot() + "/account/register")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
  }

  @Nested
  @Order(3)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class GrantAccessLevel {

    private String etag;

    @BeforeEach
    public void init() {
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
      grantChemistDataDTO.setVersion(version);
      grantAdminDataDTO.setVersion(version);
    }

    @Test
    @Order(1)
    public void grantChemist_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantChemistDataDTO)
              .post(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    public void grantAdmin_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    public void grantAdmin_secondGrant() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_ACCESS_LEVEL));
    }

    @Test
    @Order(4)
    public void grantAdmin_badVersion() {
      grantAdminDataDTO.setVersion(-1L);
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_INVALID));
    }

    @Test
    @Order(5)
    public void grantAdmin_noEtag() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_EMPTY));
    }

    @Test
    @Order(6)
    void grantAdminAccess_etagMismatch() {
      // use same etag to grant admin to other account
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/2/admin")
              .then()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_MISMATCHED_PAYLOAD));
    }
  }

  @Nested
  @Order(4)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class EditPatientData {
    private String etag;

    @BeforeEach
    public void init() {
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      patientDataDTOChangedName.setVersion(version);
    }

    @Test
    @Order(1)
    public void editPatientData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(patientDataDTOChangedName)
              .put(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("firstName", patientDataDTOChangedName.getFirstName())));
    }

    @Test
    @Order(2)
    public void editPatientData_badVersion() {
      patientDataDTOChangedName.setVersion(-1L);
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(patientDataDTOChangedName)
              .put(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_INVALID));
    }

    @Test
    @Order(3)
    public void editPatientData_concurrentEdits() {
      patientDataDTOChangedName.setLastName("Aaaaa");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(patientDataDTOChangedName)
              .put(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("firstName", patientDataDTOChangedName.getFirstName())));

      // nie zmieniamy wersji, symulacja pobrania identycznych wartości do
      // dwóch formularzy i jednoczesna edycja
      patientDataDTOChangedName.setLastName("Bbbbbb");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(patientDataDTOChangedName)
              .put(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
    }
  }

  @Nested
  @Order(5)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class EditChemistData {
    private String etag;

    @BeforeEach
    public void init() {
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      chemistDataDTOChangedLiscence.setVersion(version);
    }

    @Test
    @Order(1)
    public void editChemistData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(chemistDataDTOChangedLiscence)
              .put(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("licenseNumber", chemistDataDTOChangedLiscence.getLicenseNumber())));
    }

    @Test
    @Order(2)
    public void editChemistData_badVersion() {
      chemistDataDTOChangedLiscence.setVersion(-1L);
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(chemistDataDTOChangedLiscence)
              .put(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_INVALID));
    }

    @Test
    @Order(3)
    public void editChemistData_concurrentEdits() {
      chemistDataDTOChangedLiscence.setLicenseNumber("234567");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(chemistDataDTOChangedLiscence)
              .put(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("licenseNumber", chemistDataDTOChangedLiscence.getLicenseNumber())));

      // nie zmieniamy wersji, symulacja pobrania identycznych wartości do
      // dwóch formularzy i jednoczesna edycja
      chemistDataDTOChangedLiscence.setLicenseNumber("654321");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(chemistDataDTOChangedLiscence)
              .put(getApiRoot() + "/account/3/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
    }
  }

  @Nested
  @Order(6)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class EditAdminData {
    private String etag;

    @BeforeEach
    public void init() {
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      adminDataDTOChangedPhone.setVersion(version);
    }

    @Test
    @Order(1)
    public void editAdminData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(adminDataDTOChangedPhone)
              .put(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("workPhoneNumber", adminDataDTOChangedPhone.getWorkPhoneNumber())));
    }

    @Test
    @Order(2)
    public void editAdminData_badVersion() {
      adminDataDTOChangedPhone.setVersion(-1L);
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(adminDataDTOChangedPhone)
              .put(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_INVALID));
    }

    @Test
    @Order(3)
    public void editAdminData_concurrentEdits() {
      adminDataDTOChangedPhone.setWorkPhoneNumber("123456789");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(adminDataDTOChangedPhone)
              .put(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body(
                      "accessLevels",
                      hasItem(hasEntry("workPhoneNumber", adminDataDTOChangedPhone.getWorkPhoneNumber())));

      // nie zmieniamy wersji, symulacja pobrania identycznych wartości do
      // dwóch formularzy i jednoczesna edycja
      adminDataDTOChangedPhone.setWorkPhoneNumber("987654321");
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(adminDataDTOChangedPhone)
              .put(getApiRoot() + "/account/3/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_OPTIMISTIC_LOCK));
    }
  }

  @Nested
  @Order(7)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class ReadAccount {
    @Test
    @Order(1)
    public void getSelfInfoCorrect() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels", hasItem(hasEntry("role", "ADMIN")))
              .body("login", equalTo(adminLoginDto.getLogin()));
    }

    @Test
    @Order(2)
    public void getSelfInfo_unauthorised() {
      given()
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.FORBIDDEN.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCESS_DENIED));
    }

    @Test
    @Order(3)
    public void readAccount_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("login", equalTo(patientLoginDto.getLogin()));
    }

    @Test
    @Order(4)
    public void readAccount_noSuchUser() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/1000")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
    }

    @Test
    @Order(5)
    public void readAccountAndAccessLevels_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels", hasSize(3));
    }

    @Test
    @Order(6)
    public void readAccountAndAccessLevels_noSuchUser() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/1000/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
    }

    @Test
    @Order(7)
    public void readAccounts_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("$", hasSize(equalTo(3)))
              .body("$", hasItem(hasKey("login")))
              .body("$", hasItem(hasEntry("login", adminLoginDto.getLogin())));
    }
  }

  @Nested
  @Order(8)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class BlockAndUnblockAccessLevel {
    @Test
    @Order(1)
    public void blockRoleChemist_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/chemist/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='CHEMIST'}.active", equalTo(false));
    }

    @Test
    @Order(2)
    public void blockRolePatient_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/patient/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='PATIENT'}.active", equalTo(false));
    }

    @Test
    @Order(3)
    public void blockRoleAdmin_lastAccessLevel() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/admin/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DEACTIVATE_LAST_ACCESS_LEVEL));
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='ADMIN'}.active", equalTo(true));
    }

    @Test
    @Order(4)
    public void blockRoleAdmin_deactivationOnSelf() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/1/admin/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DEACTIVATE_SELF));
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/1/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='ADMIN'}.active", equalTo(true));
    }

    @Test
    @Order(5)
    public void unblockRolePatient_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/patient/unblock")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='PATIENT'}.active", equalTo(true));
    }

    @Test
    @Order(6)
    public void unblockRoleChemist_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/chemist/unblock")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='CHEMIST'}.active", equalTo(true));
    }

    @Test
    @Order(7)
    public void blockRoleAdmin_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/admin/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("accessLevels.find{it.role=='ADMIN'}.active", equalTo(false));
    }
    @Test
    @Order(9)
    public void blockRolePatient_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .put(getApiRoot() + "/account/3/patient/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode()); //todo
    }
    @Test
    @Order(10)
    public void blockRoleAdmin_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .put(getApiRoot() + "/account/3/patient/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode()); //todo
    }

    @Test
    @Order(12)
    public void blockRolePatient_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/25/patient/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND)); //todo
    }
    @Test
    @Order(13)
    public void blockRoleAdmin_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/25/admin/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND)); //todo
    }
    @Test
    @Order(8)
    public void blockRoleChemist_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .put(getApiRoot() + "/account/2/chemist/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode()); //todo
    }
    @Test
    @Order(11)
    public void blockRoleChemist_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/25/chemist/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND)); //todo
    }
  }

  @Nested
  @Order(9)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class BlockAndUnblockAccount {
    @Test
    @Order(1)
    public void unblockAccount_alreadyUnblocked() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/unblock")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .body("active", equalTo(true));
    }

    @Test
    @Order(2)
    public void blockAccount_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .body("active", equalTo(false));
    }

    @Test
    @Order(3)
    public void blockAccount_alreadyBlocked() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/block")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .body("active", equalTo(false));
    }

    @Test
    @Order(4)
    public void unblockAccount_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/3/unblock")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .body("active", equalTo(true));
    }
  }

  @Nested
  @Order(10)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class AddAccountWithAccessLevel {
    @Test
    @Order(1)
    public void addChemist_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addChemistAccountDto)
              .post(getApiRoot() + "/account/add-chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    public void addChemist_incorrect_duplicate() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addChemistAccountDto)
              .post(getApiRoot() + "/account/add-chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_EMAIL));
    }

    @Test
    @Order(3)
    public void addChemist_incorrect_missing_field() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addChemistAccountDtoMissingField)
              .post(getApiRoot() + "/account/add-chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(4)
    public void addAdmin_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addAdminAccountDto)
              .post(getApiRoot() + "/account/add-admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(5)
    public void addAdmin_incorrect_duplicate() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addAdminAccountDto)
              .post(getApiRoot() + "/account/add-admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.CONFLICT.getStatusCode())
              .body("message", equalTo(EXCEPTION_ACCOUNT_DUPLICATE_EMAIL));
    }

    @Test
    @Order(6)
    public void addAdmin_incorrect_missing_field() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .body(addAdminAccountDtoMissingField)
              .post(getApiRoot() + "/account/add-admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }
  }

  @Nested
  @Order(11)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class Login {
    @Test
    @Order(1)
    public void login_invalidLogin() {
      LoginDTO invalidLogin = new LoginDTO("nonexistantuser", adminLoginDto.getPassword());

      given()
              .body(invalidLogin)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
              .body("message", equalTo(EXCEPTION_AUTH_BAD_CREDENTIALS));
    }

    @Test
    @Order(2)
    public void login_invalidPassword() {
      LoginDTO invalidPassword = new LoginDTO(adminLoginDto.getLogin(), "P1!ssword");

      given()
              .body(invalidPassword)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
              .body("message", equalTo(EXCEPTION_AUTH_BAD_CREDENTIALS));
    }

    @Test
    @Order(3)
    public void login_registeredButNotConfirmed() { //todo powinno być coś innego niż bad credentials
      given()
            .body(patientLoginDto)
            .post(getApiRoot() + "/auth/login")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
            .body("message", equalTo(EXCEPTION_AUTH_BAD_CREDENTIALS));
    }

    @Test
    @Order(4)
    public void login_correct() {
      given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("jwtToken", not(empty()));
    }

    @Test
    @Order(5)
    public void login_passwordIncorrectBlockAccount() {
      // reset counter
      given()
              .body(adminLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("jwtToken", not(empty()));

      for (int i = 0; i < 3; i++) {
        given()
                .body(new LoginDTO(adminLoginDto.getLogin(), "P@ssw0rd"))
                .post(getApiRoot() + "/auth/login")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
      }
      // poprawne dane logowania
      given()
              .body(adminLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.FORBIDDEN.getStatusCode())
              .body("message", equalTo(EXCEPTION_AUTH_BLOCKED_ACCOUNT));

      // stary jwt dalej działa
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode());
    }
  }

  @Nested
  @Order(12)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class EditUserAccount {

    private String etag;
    private EditAccountDTO editAccountDTO;

    @BeforeEach
    public void init() {
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
    }

    @Test
    @Order(1)
    public void editAccount_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("confirmed", equalTo(false));

      given()
              .header("authorization", "Bearer " + adminJwt).header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/3")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("email", equalTo(editAccountDTO.getEmail()), "confirmed", equalTo(true));
    }

    @Test
    @Order(2)
    public void editAccount_mismatchedPayload() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/2")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_MISMATCHED_PAYLOAD));
    }

    @Test
    @Order(3)
    public void editAccount_noSuchAccount() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/1000")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_ENTITY_NOT_FOUND));
    }

    // todo więcej testów z edit self (podobnie jak w change user password)
  }

  @Nested
  @Order(13)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class MultipleAccessLevels {

    private String etag;
    private String chemistJwt;
    private EditAccountDTO editAccountDTO;

    @BeforeEach
    public void init() {
      chemistJwt = given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().jsonPath().getString("jwtToken");
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/2")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();
      etag = response.getHeader("ETag").replace("\"", "");
      Long version = response.getBody().jsonPath().getLong("version");
      grantAdminDataDTO = GrantAdminDataDTO.builder()
              .version(version)
              .login(chemistLoginDto.getLogin())
              .workPhoneNumber("777888999")
              .build();
    }

    @Test
    @Order(1)
    void grantAdminAccessLevelToChemist() {
      // access denied
      given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account")
              .then()
              .statusCode(Response.Status.FORBIDDEN.getStatusCode());

      // grant admin
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(grantAdminDataDTO)
              .post(getApiRoot() + "/account/2/admin")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

      // denied for old jwt
      given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account")
              .then()
              .statusCode(Response.Status.FORBIDDEN.getStatusCode());

      chemistJwt = given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().jsonPath().getString("jwtToken");

      given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());
    }



    @Test
    @Order(3)
    void revokeAdminAccessLevel() {
      // has access
      given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

      // removing access level
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/2/admin/block")
              .then()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());

      chemistJwt = given()
              .body(chemistLoginDto)
              .post(getApiRoot() + "/auth/login")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().jsonPath().getString("jwtToken");

      // no access
      given()
              .header("authorization", "Bearer " + chemistJwt)
              .get(getApiRoot() + "/account")
              .then()
              .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
  }

  @Nested
  @Order(14)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class changeLanguageVersion {
    private String etag;
    private String chemistJwt;
    private EditAccountDTO editAccountDTO;

    @Test
    @Order(1)
    public void changeLanguage_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("language", equalTo("en"));

      String newLanguage = "pl";
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/change-language?language=" + newLanguage)
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());

      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("language", equalTo(newLanguage));
    }

    @Test
    @Order(2)
    public void changeLanguage_notALanguage() {
      String newLanguage = "ajhsdkjs";
      given()
              .header("authorization", "Bearer " + adminJwt)
              .put(getApiRoot() + "/account/change-language?language=" + newLanguage)
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode())
              .body("message", equalTo(EXCEPTION_LANGUAGE_NOT_FOUND));
    }

    @Test
    @Order(3)
    public void changeLanguage_unhandledLanguage() {
      String newLanguage = "sk";
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/change-language?language=" + newLanguage)
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(4)
    public void changeLanguage_empty() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/change-language?language=")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
  }
  @Nested
  @Order(16)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class GetChemistData {
    @Test
    @Order(1)
    public void getChemistData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/2/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("role", equalTo("CHEMIST"));
    }
    @Test
    @Order(2)
    public void getChemistData_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .get(getApiRoot() + "/account/2/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    @Test
    @Order(3)
    public void getChemistData_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/35/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  @Nested
  @Order(15)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class GetAdminData {
    @Test
    @Order(1)
    public void getAdminData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/1/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("role", equalTo("ADMIN"));
    }
    @Test
    @Order(2)
    public void getAdminData_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .get(getApiRoot() + "/account/1/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    @Test
    @Order(3)
    public void getAdminData_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/35/admin")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
  }
  @Nested
  @Order(17)  //todo
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class GetPatientData {
    @Test
    @Order(1)
    public void getPatientData_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/patient")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("role", equalTo("PATIENT"));
    }
    @Test
    @Order(2)
    public void getPatientData_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .get(getApiRoot() + "/account/2/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    @Test
    @Order(3)
    public void getPatientData_no_user() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/35/chemist")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  @Nested
  @Order(18)  //todo
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class editSelfAccount {

    private String etag;
    private EditAccountDTO editAccountDTO;

    @BeforeEach
    public void init() {
      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/1")
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
              .email("hehehe@o2.pl")
              .login("admin123")
              .build();
    }

    @Test
    @Order(1)
    public void editSelfAccount_correct() {
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("email", equalTo(editAccountDTO.getEmail()));
    }
    @Test
    @Order(2)
    public void editSelfAccount_bad_version() {
      editAccountDTO.setVersion(-1L);
      given()
              .header("authorization", "Bearer " + adminJwt)
              .header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
              .body("message", equalTo(EXCEPTION_ETAG_INVALID));
    }
    @Test
    @Order(3)
    public void editSelfAccount_unauthorised() {
      given()
              .header("authorization", "Bearer " + "hehe")
              .header("If-Match", etag)
              .body(editAccountDTO)
              .put(getApiRoot() + "/account/")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
  }
}
