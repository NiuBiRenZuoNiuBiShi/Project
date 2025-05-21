package com.setravel.swifttravel.security;

import com.setravel.swifttravel.entities.Result;
import com.setravel.swifttravel.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT Authentication Interceptor
 * <p>
 * This interceptor validates JWT tokens for protected endpoints and handles
 * token renewal. It excludes the login and registration endpoints from
 * authentication requirements.
 * </p>
 *
 * @author ZhangYi
 * @version 1.0
 */
@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private ObjectMapper objectMapper;

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String RENEWED_TOKEN_HEADER = "X-Renewed-Token";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.debug("Intercepting request: {}", requestURI);

        String authHeader = request.getHeader(AUTH_HEADER);
        String token = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length());
        }

        if (!StringUtils.hasText(token)) {
            handleUnauthorizedResponse(response, "No authentication token provided");
            return false;
        }

        if (!jwtUtils.verifyToken(token)) {
            handleUnauthorizedResponse(response, "Invalid or expired token");
            return false;
        }

        Pair<String, Boolean> renewalResult = jwtUtils.renewTokenIfNecessary(token);
        if (renewalResult.getSecond()) {
            response.setHeader(RENEWED_TOKEN_HEADER, renewalResult.getFirst());
            log.debug("Token renewed for user: {}", jwtUtils.getUsernameByToken(token));
        }

        // 设置到 ThreadLocal 中
        String username = jwtUtils.getUsernameByToken(token);
        UserContext.setUsername(username);

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, @SuppressWarnings("null") Exception ex) throws Exception {
        // 清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }

    /**
     * Sends an unauthorized response with the given error message
     *
     * @param response The HTTP response
     * @param message  The error message
     * @throws Exception If there's an error writing to the response
     */
    private void handleUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Result errorResult = Result.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}