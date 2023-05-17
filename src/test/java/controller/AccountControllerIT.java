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
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantAdminDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.grant.GrantChemistDataDTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

  @Nested
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
      etag = response.getHeader("ETag");
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
    }

    @Test
    @Order(2)
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
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(3)
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
              .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
  }

  @Test
  @Order(4)
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
  @Order(5)
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
  @Order(6)
  public void readAccount_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/1")
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("login", equalTo(adminLoginDto.getLogin()));
  }

  @Test
  @Order(7)
  public void readAccount_noSuchUser() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .get(getApiRoot() + "/account/3")
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @Order(8)
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
  @Order(9)
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
  @Order(10)
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
  @Order(11)
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
  @Order(12)
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
  @Order(13)
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
  @Order(14)
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
  @Order(15)
  public void grantChemist_correct() {

    var response = given()
            .header("authorization", "Bearer " + adminJwt)
            .get(getApiRoot() + "/account/3/details")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response();
    String etag = response.getHeader("ETag");
    String licenseNumber = response.getBody().jsonPath().getString("licenseNumber");
    GrantChemistDataDTO grantChemistDataDTO1 = new GrantChemistDataDTO(licenseNumber);

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

  // access level id: 4
  @Test
  @Order(16)
  public void grantAdmin_correct() {

    var response = given()
            .header("authorization", "Bearer " + adminJwt)
            .get(getApiRoot() + "/account/3/details")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response();
    String etag = response.getHeader("ETag");
    String workPhoneNumber = response.getBody().jsonPath().getString("workPhoneNumber");
    GrantAdminDataDTO grantAdminDataDTO1 = new GrantAdminDataDTO(workPhoneNumber);

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
  @Order(17)
  public void grantAdmin_secondGrant() {

    var response = given()
            .header("authorization", "Bearer " + adminJwt)
            .get(getApiRoot() + "/account/3/details")
            .then()
            .log()
            .all()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response();
    String etag = response.getHeader("ETag");
    String workPhoneNumber = response.getBody().jsonPath().getString("workPhoneNumber");
    GrantAdminDataDTO grantAdminDataDTO1 = new GrantAdminDataDTO(workPhoneNumber);

    given()
        .header("authorization", "Bearer " + adminJwt)
            .header("If-Match", etag)
        .body(grantAdminDataDTO)
        .post(getApiRoot() + "/account/3/admin")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  @Order(18)
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
  @Order(19)
  public void editAdminData_correct() {

    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(adminDataDTOChangedPhone)
        .put(getApiRoot() + "/account/1/admin")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body(
            "accessLevels",
            hasItem(hasEntry("workPhoneNumber", adminDataDTOChangedPhone.getWorkPhoneNumber())));
  }

  @Test
  @Order(20)
  public void editChemistData_correct() {


    given()
        .header("authorization", "Bearer " + adminJwt)
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
  @Order(21)
  public void editPatientData_correct() {
    given()
        .header("authorization", "Bearer " + adminJwt)
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
  @Order(22)
  public void editPatientData_badVersion() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(patientDataDTOChangedName)
        .put(getApiRoot() + "/account/3/patient")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  @Order(23)
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
  @Order(24)
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
  @Order(25)
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
  @Order(26)
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
  @Order(27)
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
  @Order(28)
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
  @Order(29)
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
  @Order(30)
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
  @Order(31)
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
  @Order(16)
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
  @Order(32)
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
  @Order(33)
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
  @Order(34)
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
  @Order(35)
  public void editAccount_correct() {

      var response = given()
              .header("authorization", "Bearer " + adminJwt)
              .get(getApiRoot() + "/account/3/details")
              .then()
              .log()
              .all()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract()
              .response();

      String etag = response.getHeader("ETag");
      String email = response.getBody().jsonPath().getString("email");

      EditAccountDTO editAccountDTO = new EditAccountDTO(email);

    given()
        .header("authorization", "Bearer " + adminJwt).header("If-Match", etag)
        .body(editAccountDTO)
        .put(getApiRoot() + "/account/3")
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
        .statusCode(Response.Status.OK.getStatusCode())
        .body("email", equalTo(editEmailDto.getEmail()), "confirmed", equalTo(true));
  }

  @Test
  @Order(36)
  public void password_incorrect_block_account() {
    given()
        .body(patientLoginDto)
        .post(getApiRoot() + "/auth/login")
        .then()
        .statusCode(Response.Status.OK.getStatusCode());
    for (int i = 0; i < 3; i++) {
      given()
          .body(new LoginDTO(patientLoginDto.getLogin(), "P@ssw0rd"))
          .post(getApiRoot() + "/auth/login")
          .then()
          .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }
    given()
        .body(new LoginDTO(patientLoginDto.getLogin(), "P@ssw0rd"))
        .post(getApiRoot() + "/auth/login")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.FORBIDDEN.getStatusCode())
        .body("message", equalTo(EXCEPTION_AUTH_BLOCKED_ACCOUNT));
  }

  @Test
  @Order(37)
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
  @Order(38)
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
  @Order(39)
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
  @Order(40)
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

  @Test
  @Order(41)
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
  @Order(42)
  public void editAccount() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(new EditAccountDTO("kitty@meow.com"))
        .put(getApiRoot() + "/account")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("email", equalTo("kitty@meow.com"));
  }

  @Test
  @Order(43)
  public void changeUserPassword() {
    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(new UpdateOtherUserPasswordDTO("testAdm1n!23"))
        .put(getApiRoot() + "/account/1/changeUserPassword")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
    given()
        .body(changedLoginDto)
        .post(getApiRoot() + "/auth/login")
        .then()
        .log()
        .all()
        .statusCode(Response.Status.OK.getStatusCode());
  }

  @Test
  @Order(44)
  void grantAdminAccessLevelToChemist() {

    String chemistJwtLoginBeforeGrant =
        given()
            .body(grantAdminToChemistLogin)
            .post(getApiRoot() + "/auth/login")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response()
            .asString();

    String jwtFromJsonLoginBeforeGrant =
        chemistJwtLoginBeforeGrant.substring(
            chemistJwtLoginBeforeGrant.indexOf(":") + 2, chemistJwtLoginBeforeGrant.length() - 2);

    given()
        .header("authorization", "Bearer " + jwtFromJsonLoginBeforeGrant)
        .get(getApiRoot() + "/account")
        .then()
        .statusCode(Response.Status.FORBIDDEN.getStatusCode());

    given()
        .header("authorization", "Bearer " + adminJwt)
        .body(grantAdminDataDTO)
        .post(getApiRoot() + "/account/2/admin")
        .then()
        .statusCode(Response.Status.OK.getStatusCode());

    String chemistJwtAfterGrant =
        given()
            .body(grantAdminToChemistLogin)
            .post(getApiRoot() + "/auth/login")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response()
            .asString();

    String jwtFromJsonAfterGrant =
        chemistJwtAfterGrant.substring(
            chemistJwtAfterGrant.indexOf(":") + 2, chemistJwtAfterGrant.length() - 2);

    given()
        .header("authorization", "Bearer " + jwtFromJsonAfterGrant)
        .get(getApiRoot() + "/account")
        .then()
        .statusCode(Response.Status.OK.getStatusCode());
  }

  @Test
  @Order(45)
  void revokeAdminAccessLevel() {

    String chemistJwtLoginBeforeRevokeGrant =
        given()
            .body(grantAdminToChemistLogin)
            .post(getApiRoot() + "/auth/login")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response()
            .asString();

    String chemistJwtFromJsonLoginBeforeRevokeGrant =
        chemistJwtLoginBeforeRevokeGrant.substring(
            chemistJwtLoginBeforeRevokeGrant.indexOf(":") + 2,
            chemistJwtLoginBeforeRevokeGrant.length() - 2);

    given()
        .header("authorization", "Bearer " + chemistJwtFromJsonLoginBeforeRevokeGrant)
        .get(getApiRoot() + "/account")
        .then()
        .statusCode(Response.Status.OK.getStatusCode());

    given()
        .header("authorization", "Bearer " + adminJwt)
        .put(getApiRoot() + "/account/2/admin/block")
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    String chemistJwtAfterRemovingGrant =
        given()
            .body(grantAdminToChemistLogin)
            .post(getApiRoot() + "/auth/login")
            .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
            .response()
            .asString();

    String jwtFromJsonAfterGrantRemoving =
        chemistJwtAfterRemovingGrant.substring(
            chemistJwtAfterRemovingGrant.indexOf(":") + 2,
            chemistJwtAfterRemovingGrant.length() - 2);

    given()
        .header("authorization", "Bearer " + jwtFromJsonAfterGrantRemoving)
        .get(getApiRoot() + "/account")
        .then()
        .statusCode(Response.Status.FORBIDDEN.getStatusCode());
  }
}
