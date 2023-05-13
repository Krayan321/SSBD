package pl.lodz.p.it.ssbd2023.ssbd01.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class i18n {
  public static final String EXCEPTION_UNKNOWN = "exception.unknown";
  public static final String EXCEPTION_GENERAL = "exception.general";
  public static final String EXCEPTION_PERSISTENCE = "exception.persistence";
  public static final String EXCEPTION_ACCESS_DENIED = "exception.access-denied";
  public static final String EXCEPTION_TRANSACTION_ROLLBACK = "exception.transaction-rollback";
  public static final String EXCEPTION_OPTIMISTIC_LOCK = "exception.optimistic-lock";
  public static final String EXCEPTION_ENTITY_NOT_FOUND = "exception.entity-not-found";

  public static final String EXCEPTION_ACCOUNT_CONSTRAINT_VIOLATION =
      "exception.account.constraint-violation";
  public static final String EXCEPTION_ACCOUNT_DELETION_CONFIRMED =
      "exception.account.deletion-confirmed";
  public static final String EXCEPTION_ACCOUNT_NO_SUCH_ACCESS_LEVEL =
      "exception.account.no-such-access-level";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_ACCESS_LEVEL =
      "exception.account.duplicate.access-level";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_EMAIL =
          "exception.account.duplicate.email";
  public static final String EXCEPTION_ACCOUNT_DUPLICATE_LOGIN =
          "exception.account.duplicate.login";

  public static final String EXCEPTION_AUTH_BAD_CREDENTIALS = "exception.auth.bad-credentials";
  public static final String EXCEPTION_AUTH_BLOCKED_ACCOUNT = "exception.auth.blocked-account";

  public static final String EXCEPTION_TOKEN_EXPIRED = "exception.token.expired";
  public static final String EXCEPTION_TOKEN_NOT_FOUND = "exception.token.not-found";
  public static final String EXCEPTION_TOKEN_ALREADY_USED = "exception.token.already-used";
  public static final String EXCEPTION_TOKEN_BAD_TYPE = "exception.token.bad-type";

  public static final String MAIL_ACCOUNT_BLOCKED_SUBJECT = "mail.account.blocked.subject";
  public static final String MAIL_ACCOUNT_BLOCKED_BODY = "mail.account.blocked.body";
  public static final String MAIL_ACCOUNT_UNBLOCKED_SUBJECT = "mail.account.unblocked.subject";
  public static final String MAIL_ACCOUNT_UNBLOCKED_BODY = "mail.account.unblocked.body";
  public static final String MAIL_ACCOUNT_ACTIVATED_SUBJECT = "mail.account.activated.subject";
  public static final String MAIL_ACCOUNT_ACTIVATED_BODY = "mail.account.activated.body";

  public static String getMessage(String message, Locale locale) {
    ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", locale);
    return bundle.getString(message);
  }
}
