package pl.lodz.p.it.ssbd2023.ssbd01.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.interceptor.Interceptors;
import org.eclipse.microprofile.config.ConfigProvider;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;

import java.text.ParseException;

@Interceptors(TrackerInterceptor.class)
public class EntityIdentitySignerVerifier {

  private static String ETAG_SECRET = ConfigProvider.getConfig().getValue("etag.secret", String.class);

  public static void checkEtagIntegrity(SignableEntity entity, String etag) {
    if (!verifyEntityIntegrity(entity, etag)) {
      throw ApplicationException.createEtagNotValidException();
    }
  }

  public static String calculateEntitySignature(SignableEntity entity) {
    try {
      JWSSigner signer = new MACSigner(ETAG_SECRET);

      JWSObject jwsObject =
          new JWSObject(
              new JWSHeader(JWSAlgorithm.HS256), new Payload(entity.getSignablePayload()));
      jwsObject.sign(signer);
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw ApplicationException.createEtagCreationException();
    }
  }

  public static boolean validateEntitySignature(String tag) {
    try {
      JWSObject jwsObject = JWSObject.parse(tag);
      JWSVerifier verifier = new MACVerifier(ETAG_SECRET);
      return jwsObject.verify(verifier);

    } catch (ParseException | JOSEException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean verifyEntityIntegrity(SignableEntity entity, String tag) {
    try {
      final String header = JWSObject.parse(tag).getPayload().toString();
      final String signableEntityPayload = entity.getSignablePayload();
      return validateEntitySignature(tag) && signableEntityPayload.equals(header);

    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
  }
}
