package link.team71.emailserver.middleware;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import link.team71.emailserver.exception.JwtException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtGenerator {

    private final Dotenv dotenv = Dotenv.load();
    private final String secret = dotenv.get("AUTH_SECRET");

    public String generateJwt(Map<String, Object> claims, String issuer) {

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60*60))
                .withPayload(claims)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, Object> verifyJwt(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

            String payload = new String(Base64.getUrlDecoder().decode(jwt.getPayload()));
           // System.out.println(payload);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);
            return claims;
        }catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            throw new JwtException(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new JwtException(e.getMessage());
        }
    }

    boolean isExpire(String token) {
        return  JWT.decode(token).getExpiresAt().before(new Date());
    }

    public boolean validateJwt(String token, String userDetails) {
        return !isExpire(token) && token.trim().equals(userDetails);
    }
}
