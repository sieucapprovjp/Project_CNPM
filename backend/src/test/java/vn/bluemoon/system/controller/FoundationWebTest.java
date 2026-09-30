package vn.bluemoon.system.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import vn.bluemoon.common.exception.ApiExceptionHandler;
import vn.bluemoon.config.RequestIdFilter;
import vn.bluemoon.config.SecurityConfig;

@WebMvcTest(controllers = HealthController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class, properties = {
    "FRONTEND_ORIGIN=http://localhost:5173", "springdoc.api-docs.enabled=false"})
@Import({SecurityConfig.class, RequestIdFilter.class, ApiExceptionHandler.class,
    FoundationWebTest.ErrorProbeController.class, FoundationWebTest.ProbeSecurity.class})
class FoundationWebTest {
  @Autowired MockMvc mvc;
  @Autowired ObjectMapper mapper;

  @Test
  void healthMatchesContractWithoutAuthentication() throws Exception {
    mvc.perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json("{\"status\":\"ok\"}", org.springframework.test.json.JsonCompareMode.STRICT))
        .andExpect(header().doesNotExist("Set-Cookie"));
  }

  @Test
  void unknownApiIsDeniedWithoutRedirectOrSession() throws Exception {
    var response = mvc.perform(get("/api/v1/not-yet-implemented").header("X-Request-Id", "untrusted"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error.code").value("UNAUTHENTICATED"))
        .andExpect(header().doesNotExist("Location"))
        .andExpect(header().doesNotExist("Set-Cookie"))
        .andReturn().getResponse();
    String requestId = response.getHeader("X-Request-Id");
    java.util.UUID.fromString(requestId);
    assertEquals(requestId, mapper.readTree(response.getContentAsString()).at("/error/requestId").asText());
    assertNull(MDC.get("requestId"));
  }

  @Test
  void authenticatedRequestsAreStillDeniedUntilPermissionsExist() throws Exception {
    mvc.perform(get("/api/v1/not-yet-implemented").with(user("test-user")))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"));
  }

  @Test
  void documentationAndLoginAreClosedByDefault() throws Exception {
    for (String path : new String[]{"/v3/api-docs", "/swagger-ui/index.html", "/login"}) {
      mvc.perform(get(path)).andExpect(status().isUnauthorized());
    }
  }

  @Test
  void postCannotBypassHealthSecurity() throws Exception {
    mvc.perform(post("/health")).andExpect(status().isUnauthorized());
  }

  @Test
  void allowsOnlyConfiguredFrontendOrigin() throws Exception {
    mvc.perform(options("/api/v1/example")
            .header("Origin", "http://localhost:5173")
            .header("Access-Control-Request-Method", "GET")
            .header("Access-Control-Request-Headers", "Authorization"))
        .andExpect(status().isOk())
        .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    mvc.perform(options("/api/v1/example")
            .header("Origin", "https://untrusted.example")
            .header("Access-Control-Request-Method", "GET"))
        .andExpect(status().isForbidden())
        .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
  }

  @Test
  void invalidBodyReturnsTyped422Details() throws Exception {
    mvc.perform(post("/__test/body").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.error.code").value("VALIDATION_FAILED"))
        .andExpect(jsonPath("$.error.details[0].field").value("name"))
        .andExpect(jsonPath("$.error.details[0].code").value("INVALID_VALUE"));
  }

  @Test
  void malformedBodyDoesNotExposeRejectedContent() throws Exception {
    mvc.perform(post("/__test/body").contentType(MediaType.APPLICATION_JSON).content("secret-not-json"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error.code").value("MALFORMED_REQUEST"))
        .andExpect(content().string(not(containsString("secret-not-json"))));
  }

  @Test
  void differentiatesInvalidParameterValueFromMalformedType() throws Exception {
    mvc.perform(get("/__test/page").param("page", "-1"))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.error.code").value("VALIDATION_FAILED"));
    mvc.perform(get("/__test/page").param("page", "abc"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error.code").value("MALFORMED_REQUEST"));
  }

  @Test
  void databaseConflictReturns409WithoutDatabaseDetails() throws Exception {
    mvc.perform(get("/__test/conflict"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error.code").value("DATA_CONFLICT"))
        .andExpect(content().string(not(containsString("secret-db-value"))));
  }

  @Test
  void unexpectedErrorsAreSafeAndTraceable() throws Exception {
    mvc.perform(get("/__test/failure"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error.code").value("INTERNAL_ERROR"))
        .andExpect(jsonPath("$.error.requestId").isNotEmpty())
        .andExpect(content().string(not(containsString("secret-internal-value"))));
  }

  @Test
  void missingResourceReturnsShared404ShapeWhenAuthorized() throws Exception {
    mvc.perform(get("/__test/missing"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error.code").value("RESOURCE_NOT_FOUND"));
  }

  @TestConfiguration
  static class ProbeSecurity {
    @Bean
    @Order(0)
    SecurityFilterChain testOnlyRoutes(HttpSecurity http) throws Exception {
      return http.securityMatcher("/__test/**").csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).build();
    }
  }

  @RestController
  @TestComponent
  static class ErrorProbeController {
    record Probe(@NotBlank String name) {}
    @PostMapping("/__test/body") Probe body(@Valid @RequestBody Probe body) { return body; }
    @GetMapping("/__test/page") int page(@RequestParam @Min(0) int page) { return page; }
    @GetMapping("/__test/conflict") void conflict() { throw new DataIntegrityViolationException("secret-db-value"); }
    @GetMapping("/__test/failure") void failure() { throw new IllegalStateException("secret-internal-value"); }
  }
}
