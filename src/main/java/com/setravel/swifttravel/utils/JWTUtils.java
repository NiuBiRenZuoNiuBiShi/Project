package com.setravel.swifttravel.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for JWT token operations such as generation, verification,
 * renewal, and blacklisting.
 * 
 * @author SwiftTravel
 */
@Component
@Slf4j
public class JWTUtils {
    private static final String SECRET_KEY = "your-secret-key"; // Consider moving to configuration
    private static final long EXPIRE_TIME = 86400000; // 1 day in milliseconds
    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";
    private static final long RENEWAL_THRESHOLD = 600000L; // 续签阈值，10分钟（单位：毫秒）

    private final StringRedisTemplate redisTemplate;

    public JWTUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Generates a JWT Token
     * <p>
     * This method generates a JWT Token based on the username with a validity period of 1 day.
     * It uses the HMAC256 algorithm for encryption and stores the username as the Subject in the Token.
     *
     * @param username The username
     * @return The generated JWT Token
     */
    public String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new RuntimeException("Could not generate token", e);
        }
    }

    /**
     * Verifies the validity of a JWT Token
     * <p>
     * This method checks if the provided Token is empty and if it's on the blacklist.
     * If the Token is valid, it returns true; otherwise, it returns false.
     *
     * @param token The JWT Token
     * @return True if the Token is valid, false otherwise
     */
    public boolean verifyToken(String token) {
        if (token == null || token.isEmpty() || isTokenInBlockList(token)) {
            return false;
        }
        try {
            decodeToken(token);
            return true;
        } catch (Exception e) {
            log.warn("Invalid token encountered: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a JWT Token is about to expire and renews it if necessary
     * <p>
     * This method checks if the provided Token is close to expiration (less than 10 minutes remaining).
     * If it is, a new Token is generated and returned; otherwise, the original Token is returned.
     *
     * @param token The JWT Token
     * @return A Pair containing the new Token (if renewal is needed) and a flag indicating whether a renewal occurred
     */
    public Pair<String, Boolean> renewTokenIfNecessary(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);
            long expirationTime = decodedJWT.getExpiresAt().getTime();
            long currentTime = System.currentTimeMillis();

            // Renew if expiration time is less than the threshold
            if (expirationTime - currentTime < RENEWAL_THRESHOLD) {
                String username = decodedJWT.getSubject();
                return Pair.of(generateToken(username), true); // Generate new Token
            }
            return Pair.of(token, false); // No renewal needed, return original Token
        } catch (Exception e) {
            log.error("Error while checking token for renewal: {}", e.getMessage(), e);
            return Pair.of(token, false); // Return original token on error
        }
    }

    /**
     * Adds a Token to the blacklist
     * <p>
     * This method stores the provided Token in the Redis blacklist with an expiration of 1 day.
     * Making this method public to be accessible from service layer.
     *
     * @param token The JWT Token to blacklist
     */
    public void addTokenToBlockList(String token) {
        try {
            // Extract expiration time from token to set appropriate TTL
            DecodedJWT jwt = JWT.decode(token);
            long expiresAt = jwt.getExpiresAt().getTime();
            long currentTime = System.currentTimeMillis();
            long ttl = Math.max(1, (expiresAt - currentTime) / 1000); // Convert to seconds, minimum 1 second
            
            redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", ttl, TimeUnit.SECONDS);
            log.debug("Token added to blacklist with TTL: {} seconds", ttl);
        } catch (Exception e) {
            log.error("Error adding token to blacklist: {}", e.getMessage(), e);
            // Still try to add with default expiry if decoding fails
            redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Checks if a Token is in the blacklist
     * <p>
     * This method checks if the provided Token exists in the Redis blacklist.
     *
     * @param token The JWT Token
     * @return True if the Token is in the blacklist, false otherwise
     */
    private boolean isTokenInBlockList(String token) {
        try {
            Boolean hasKey = redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
            return hasKey != null && hasKey;
        } catch (Exception e) {
            log.error("Error checking token in blacklist: {}", e.getMessage(), e);
            return false; // Assume token is valid if Redis check fails
        }
    }

    /**
     * Extracts the username from a JWT Token
     * <p>
     * This method decodes the JWT Token and extracts the username (Subject).
     *
     * @param token The JWT Token
     * @return The username extracted from the Token
     */
    public String getUsernameByToken(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Decodes and verifies a JWT Token
     * <p>
     * This method uses the HMAC256 algorithm to decode and verify the JWT Token.
     * If the Token is invalid or expired, an exception is thrown.
     *
     * @param token The JWT Token
     * @return The decoded JWT object
     */
    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
    }
}