package com.setravel.swifttravel.config;

import com.setravel.swifttravel.security.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

/**
 * Web MVC Configuration
 * <p>
 * This configuration class sets up the JWT interceptor for all API endpoints
 * except for user registration and login.
 * </p>
 *
 * @author SwiftTravel
 * @version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JWTInterceptor jwtInterceptor;
    /**
     * Registers the JWT interceptor for all API endpoints except excluded paths
     *
     * @param registry The interceptor registry
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // Apply to all API endpoints
                .excludePathPatterns(
                        "/api/user/login",     // Exclude login endpoint
                        "/api/user/register"   // Exclude registration endpoint
                );
    }

    /**
     * Creates an ObjectMapper bean for JSON serialization/deserialization
     *
     * @return The ObjectMapper instance
     */
    /*
    @Bean
    public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
        return new com.fasterxml.jackson.databind.ObjectMapper();
    }*/
}