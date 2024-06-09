package id.nolimit.api.blogpost.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.nolimit.api.blogpost.entity.LoginAbleUser;
import id.nolimit.api.blogpost.exception.CustomRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${blogpost.service.jjwt.secret}")
    private String secret;

    @Value("${blogpost.service.jjwt.expiration}")
    private String expirationTime;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .build()
                .parseClaimsJws(token).getBody();
    }

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).get("email", String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return false;
        }
    }

    public String generateToken(LoginAbleUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("email", user.getEmail());

        ObjectMapper objectMapper = new ObjectMapper();
        String subject;
        try {
            subject = objectMapper.writeValueAsString(claims);
        } catch (JsonProcessingException e) {
            throw new CustomRuntimeException(e);
        }
        return doGenerateToken(claims, subject);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long expirationTimeLong = Long.parseLong(expirationTime); //in second

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}
