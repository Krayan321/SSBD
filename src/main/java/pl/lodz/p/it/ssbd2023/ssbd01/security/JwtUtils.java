package pl.lodz.p.it.ssbd2023.ssbd01.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class JwtUtils {

    @Inject
    @ConfigProperty(name = "jwt.timeout")
    private int TIMEOUT;

    @Inject
    @ConfigProperty(name = "jwt.key")
    private String SECRET;


    public String create(CredentialValidationResult result) {
        String principal = result.getCallerPrincipal().getName();
        Set<String> authorities = result.getCallerGroups();

        return Jwts.builder()
                .setSubject(principal)
                .claim("roles", String.join(",", authorities))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .setExpiration(new Date(ZonedDateTime.now().plusMinutes(TIMEOUT).toInstant().toEpochMilli()))
                .compact();
    }

    public Claims load(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
