package com.setravel.swifttravel.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JWTUtils {
    private static final String SECRET_KEY = "your-secret-key";
    private static final long EXPIRE_TIME = 86400000; // 1 day in milliseconds
    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";
    private static final long RENEWAL_THRESHOLD = 600000L; // 续签阈值，10分钟（单位：毫秒）

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public JWTUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成JWT Token
     * <p>
     * 该方法根据用户名生成一个JWT Token，并设置有效期为1天。
     * 使用HMAC256算法加密Token，并将用户名作为Subject保存到Token中。
     *
     * @param username 用户名
     * @return 生成的JWT Token
     */
    public String generateToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(algorithm);
    }

    /**
     * 验证JWT Token的合法性
     * <p>
     * 该方法验证传入的Token是否为空，并检查Token是否在黑名单中。如果Token合法，则返回true，否则返回false。
     *
     * @param token JWT Token
     * @return 如果Token合法，则返回true，否则返回false
     */
    public boolean verifyToken(String token) {
        if (token.isEmpty() || isTokenInBlockList(token)) {
            return false;
        }
        try {
            decodeToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查JWT Token是否快过期，并进行续签
     * <p>
     * 该方法检查传入的Token是否接近过期（距离过期时间小于10分钟）。如果是，则生成一个新的Token并返回，
     * 否则返回原始Token。
     *
     * @param token JWT Token
     * @return 返回一个Pair，包含新的Token（如果需要续签），以及是否进行续签的标志
     */
    public Pair<String, Boolean> renewTokenIfNecessary(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        long expirationTime = decodedJWT.getExpiresAt().getTime();
        long currentTime = System.currentTimeMillis();

        // 如果距离过期时间小于续签阈值，进行续签
        if (expirationTime - currentTime < RENEWAL_THRESHOLD) {
            String username = decodedJWT.getSubject();
            return Pair.of(generateToken(username), true); // 生成新的Token
        }
        return Pair.of(token, false); // 不需要续签，返回原始Token
    }

    /**
     * 将Token添加到黑名单
     * <p>
     * 该方法将传入的Token存储到Redis的黑名单中，设置过期时间为1天。
     *
     * @param token JWT Token
     */
    @SuppressWarnings("unused")
    private void addTokenToBlockList(String token) {
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "true", EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查Token是否在黑名单中
     * <p>
     * 该方法检查传入的Token是否存在于Redis的黑名单中。
     *
     * @param token JWT Token
     * @return 如果Token在黑名单中，则返回true，否则返回false
     */
    private boolean isTokenInBlockList(String token) {
        return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
    }

    /**
     * 从JWT Token中获取用户名
     * <p>
     * 该方法解析JWT Token并提取其中的用户名（Subject）。
     *
     * @param token JWT Token
     * @return 从Token中提取的用户名
     */
    public String getUsernameByToken(String token) {
        return JWT
                .decode(token)
                .getSubject();
    }

    /**
     * 解码JWT Token
     * <p>
     * 该方法使用HMAC256算法解码并验证JWT Token。如果Token无效或过期，将抛出异常。
     *
     * @param token JWT Token
     * @return 解码后的DecodedJWT对象
     */
    private DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
    }
}
