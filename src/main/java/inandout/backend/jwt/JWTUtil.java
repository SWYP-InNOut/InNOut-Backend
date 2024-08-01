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
    private final Long ACCESSTOKEN_VALIDTIME = (60 * 1000L) * 30; // 30분
    private final Long REFRESHTOKEN_VALIDTIME = (60 * 1000L) * 60 * 24 * 7; // 7일
    private final Long LINKTOKEN_VALIDTIME = (60 * 1000L) * 15; // 15분

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

//    public String getEmail(String token) {
//
//        String email;
//        email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
//        if (email == null) {
//            email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sub", String.class);
//        }
//        return email;
//       // return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
//
//    }

    public Integer getMemberId(String token) {
        Integer memberId;
        memberId = Integer.valueOf(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject());
//        if (memberId == null) {
//            email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sub", String.class);
//        }
        return memberId;
        // return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);

    }

    public Boolean isExpired(String token) throws ExpiredJwtException{
        System.out.println("isExpired: "+token);
        System.out.println("secretKey: "+secretKey);
        System.out.println(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date(System.currentTimeMillis())));
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String createAccessToken(int memberId, Long expiredMs) {
        Claims claims = Jwts.claims().subject(String.valueOf(memberId)).build();
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

    public TokenInfo generateToken(int memberId) {
        String accessToken = createAccessToken(memberId, ACCESSTOKEN_VALIDTIME);
        String refreshToken = createRefreshToken(REFRESHTOKEN_VALIDTIME);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateLinkToken(int roomId) {
        Claims claims = Jwts.claims().subject(String.valueOf(roomId)).build();
        claims.put("ROLD_ANONYMOUS", true);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + LINKTOKEN_VALIDTIME))
                .signWith(secretKey)
                .compact();
    }
}
