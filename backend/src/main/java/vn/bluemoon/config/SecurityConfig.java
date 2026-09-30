package vn.bluemoon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.bluemoon.common.exception.ApiError;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper mapper,
      CorsConfigurationSource corsConfigurationSource,
      @Value("${springdoc.api-docs.enabled:false}") boolean docsEnabled) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .requestCache(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers(HttpMethod.GET, "/health").permitAll();
          if (docsEnabled) {
            auth.requestMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/**",
                "/v3/api-docs.yaml", "/swagger-ui.html", "/swagger-ui/**").permitAll();
          }
          // Authentication and feature permissions will replace this guard in their own milestone.
          auth.anyRequest().denyAll();
        })
        .exceptionHandling(errors -> errors
            .authenticationEntryPoint((request, response, exception) ->
                writeError(mapper, response, 401, "UNAUTHENTICATED", "Authentication is required."))
            .accessDeniedHandler((request, response, exception) ->
                writeError(mapper, response, 403, "ACCESS_DENIED", "Access is denied.")));
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource(@Value("${FRONTEND_ORIGIN}") String origin) {
    URI uri = URI.create(origin);
    if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
        || uri.getHost() == null || uri.getUserInfo() != null || uri.getQuery() != null
        || uri.getFragment() != null || (uri.getPath() != null && !uri.getPath().isEmpty())) {
      throw new IllegalArgumentException("FRONTEND_ORIGIN must be an exact HTTP(S) origin without a trailing slash.");
    }
    CorsConfiguration cors = new CorsConfiguration();
    cors.setAllowedOrigins(List.of(origin));
    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    cors.setExposedHeaders(List.of("X-Request-Id"));
    cors.setAllowCredentials(false);
    cors.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cors);
    return source;
  }

  private static void writeError(ObjectMapper mapper, HttpServletResponse response, int status,
      String code, String message) throws IOException {
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    mapper.writeValue(response.getOutputStream(), ApiError.of(code, message));
  }
}
