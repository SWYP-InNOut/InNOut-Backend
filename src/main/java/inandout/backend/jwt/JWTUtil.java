package inandout.backend.jwt;

import inandout.backend.dto.login.CustomMemberDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;
    private final Long accessTokenValidTime = (60 * 1000L) * 30; // 30분
    private final Long refreshTokenValidTime = (60 * 1000L) * 60 * 24 * 7; // 7일

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Boolean isExpired(String token) throws ExpiredJwtException{

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String createAccessToken(String email, Long expiredMs) {
        Claims claims = Jwts.claims().subject(email).build();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long expiredMs) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public TokenInfo generateToken(String email) {
        String accessToken = createAccessToken(email, accessTokenValidTime);
        String refreshToken = createRefreshToken(refreshTokenValidTime);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
