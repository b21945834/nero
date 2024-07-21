package tr.com.nero.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tr.com.nero.authentication.ws.schemas.JwtTokenResponse;
import tr.com.nero.user.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "a9bdbe216113f1e5add863110d5bf17b24fb08d4ccde64c54cce232b74d7ef3a";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public JwtTokenResponse generateToken(User userDetails) {
        Map<String, Object> map = new HashMap<>();
        map.put("role", userDetails.getRole().getRole());
        Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 30 * 120);
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();
        jwtTokenResponse.setExpireDate(expireDate);
        jwtTokenResponse.setToken(generateToken(map, userDetails, expireDate));
        return jwtTokenResponse;
    }

    public String generateToken(Map<String, Object> extraClaims,
                                User userDetails, Date expireDate) {
        return Jwts.builder().
                claims(extraClaims).
                subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).after(extractExpiration(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
