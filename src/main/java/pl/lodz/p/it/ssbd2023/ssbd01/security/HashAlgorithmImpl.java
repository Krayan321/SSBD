package pl.lodz.p.it.ssbd2023.ssbd01.security;

import jakarta.security.enterprise.identitystore.PasswordHash;
import org.apache.commons.codec.digest.DigestUtils;

public class HashAlgorithmImpl implements PasswordHash {

    public static boolean check(String password, String hashedPassword) {
        String toVerify = generate(password);
        return hashedPassword.equals(toVerify);
    }

    public static String generate(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public String generate(char[] password) {
        return DigestUtils.sha256Hex(new String(password));
    }

    public boolean verify(char[] password, String hashedPassword) {
        String toVerify = DigestUtils.sha256Hex(new String(password));
        return hashedPassword.equals(toVerify);
    }


}
