package pl.lodz.p.it.ssbd2023.ssbd01.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;

@Log
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

        System.out.println(SECRET);
        log.warning(SECRET);

        return Jwts.builder()
                .setSubject(principal)
                .claim("roles", String.join(",", authorities))
                .setExpiration(new Date(ZonedDateTime.now().plusMinutes(TIMEOUT).toInstant().toEpochMilli()))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey getSecretKey(){
        byte[] encodeKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(encodeKey);
    }

    public Claims load(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
