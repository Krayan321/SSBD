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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChangePasswordDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.EditAccountDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.auth.LoginDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerIT extends BaseTest {
  static String adminJwt;

  static String patientJwt;

  @BeforeAll
  static void setUp() throws InterruptedException {
    System.out.println(getApiRoot());
    adminJwt =
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

    // globally set authorization header
    RestAssured.requestSpecification =
        new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .log(LogDetail.ALL)
            // .addHeader("authorization", "Bearer " + adminJwt)
            .build();
  }

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
        .body("message", equalTo(EXCEPTION_UNAUTHORISED));
  }

  @Test
  @Order(2)
  public void readAccount_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/1")
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("login", equalTo(adminLoginDto.getLogin()));
  }

  @Test
  @Order(3)
  public void readAccount_noSuchUser() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2")
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @Order(4)
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
  @Order(5)
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
  @Order(6)
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
  @Order(7)
  public void cannotLogin_when_registered_but_account_not_confirmed() {
    patientJwt =
        given()
            .body(patientLoginDto)
            .post(getApiRoot() + "/auth/login")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
            .extract()
            .response()
            .asString();
  }

  // access level id: 3
  @Test
  @Order(8)
  public void grantChemist_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(grantChemistDataDTO)
        .put(getApiRoot() + "/account/2/grantChemist")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
  }

  // access level id: 4
  @Test
  @Order(9)
  public void grantAdmin_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(grantAdminDataDTO)
        .put(getApiRoot() + "/account/2/grantAdmin")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
  }

// todo this shouldn't pass
  @Test
  @Order(10)
  public void grantAdmin_secondGrant() {
    given()
            .header("authorization", "Bearer " + adminJwt)
            .body(grantAdminDataDTO)
            .put(getApiRoot() + "/account/2/grantAdmin")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.CONFLICT.getStatusCode());
  }


  @Test
  @Order(10)
  public void readAccountAndAccessLevels_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2/details")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("accessLevels", hasSize(3));
  }

  @Test
  @Order(11)
  public void editAdminData_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(adminDataDTOChangedPhone)
        .put(getApiRoot() + "/account/2/admin")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body(
            "accessLevels",
            hasItem(hasEntry("workPhoneNumber", adminDataDTOChangedPhone.getWorkPhoneNumber())));
  }

  @Test
  @Order(11)
  public void editChemistData_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(chemistDataDTOChangedLiscence)
        .put(getApiRoot() + "/account/2/chemist")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body(
            "accessLevels",
            hasItem(hasEntry("licenseNumber", chemistDataDTOChangedLiscence.getLicenseNumber())));
  }

  @Test
  @Order(11)
  public void editPatientData_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(patientDataDTOChangedName)
        .put(getApiRoot() + "/account/2/patient")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body(
            "accessLevels",
            hasItem(hasEntry("firstName", patientDataDTOChangedName.getFirstName())));
  }

  @Test
  @Order(12)
  public void editPatientData_badVersion() {
    given()
            .header("authorization", "Bearer " + adminJwt)
            .body(patientDataDTOChangedName)
            .put(getApiRoot() + "/account/2/patient")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  @Order(12)
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
  @Order(13)
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
  @Order(14)
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
  @Order(15)
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
  @Order(16)
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
  @Order(17)
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

  @Test
  @Order(18)
  public void password_incorrect_block_account() {
    for (int i = 0; i < 3; i++) {
      given()
          .body(new LoginDTO(patientLoginDto.getLogin(), "invalid_test"))
          .post(getApiRoot() + "/auth/login")
          .then()
          .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    given()
        .body(adminLoginDto)
        .post(getApiRoot() + "/auth/login")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
        .extract()
        .response()
        .asString();
  }

  @Test
  @Order(19)
  public void changeOwnPassword_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(new ChangePasswordDTO("admin123", "admin321"))
        .put(getApiRoot() + "/account/1/changePassword")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
  }

  @Test
  @Order(20)
  public void unblockAccount_alreadyUnblocked() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .put(getApiRoot() + "/account/2/unblock")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2")
        .then()
        .log()
        .all()
        .body("active", equalTo(true));
  }

  @Test
  @Order(21)
  public void blockAccount_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .put(getApiRoot() + "/account/2/block")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2")
        .then()
        .log()
        .all()
        .body("active", equalTo(false));
  }

  @Test
  @Order(22)
  public void blockAccount_alreadyBlocked() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .put(getApiRoot() + "/account/2/block")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2")
        .then()
        .log()
        .all()
        .body("active", equalTo(false));
  }

  @Test
  @Order(23)
  public void unblockAccount_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .put(getApiRoot() + "/account/2/unblock")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/2")
        .then()
        .log()
        .all()
        .body("active", equalTo(true));
  }

  @Test
  @Order(24)
  public void readAccounts_returns_something() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/")
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("$", hasSize(greaterThan(3)))
        .body("$", hasItem(hasKey("login")))
        .body("$", hasItem(hasEntry("login", adminLoginDto.getLogin())));
  }

  @Test
  @Order(25)
  public void editAccount() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(new EditAccountDTO("kitty@meow.com"))
        .put(getApiRoot() + "/account/1/editAccount")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/1")
        .then()
        .log()
        .all()
        .body("email", equalTo("admin@o2.pl"));
  }

  @Test
  @Order(24)
  public void getSelfInfoCorrect(){
    given()
            .header("authorization", "Bearer " + adminJwt)
            .get(getApiRoot() + "/account/details")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.OK.getStatusCode())
            .body("accessLevels",
                    hasItem(hasEntry("role", "ADMIN")))
            .body("login", equalTo(adminLoginDto.getLogin()) );
  }

}
