package cc.phos.utils;

import cc.phos.entity.UserEntity;
import cc.phos.security.AuthType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class TokenUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private Long expiration;

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String generateToken(UserEntity userEntity, AuthType authType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "access_token");
        map.put("auth_type", authType);
        return buildToken(map, userEntity, expiration);
    }

//    private String generateToken(Map<String, Object> extraClaims, UserEntity userEntity) {
//        return buildToken(extraClaims, userEntity, expiration);
//    }

    public String generateRefreshToken(UserEntity userEntity, AuthType authType) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "refresh_token");
        map.put("auth_type", authType);
        return buildToken(map, userEntity, refreshExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isAccessToken(String token) {
        try {
            return extractClaim(token, claims -> Objects.equals(claims.get("type"), "access_token"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            return extractClaim(token, claims -> Objects.equals(claims.get("type"), "refresh_token"));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public AuthType extractAuthType(String token) {
        return extractClaim(token, claims -> AuthType.valueOf(Objects.toString(claims.get("auth_type"))));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private String buildToken(Map<String, Object> extraClaims, UserEntity userEntity, Long expiration) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userEntity.getMobile()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
