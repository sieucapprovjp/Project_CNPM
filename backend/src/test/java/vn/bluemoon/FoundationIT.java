package vn.bluemoon;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class FoundationIT {
  @Autowired JdbcTemplate jdbc;
  @Autowired Flyway flyway;
  @Autowired TestRestTemplate http;
  @Autowired ApplicationContext context;

  @Test
  void bootsAgainstPostgres18WithFlywayAndServesHealth() {
    int version = Integer.parseInt(jdbc.queryForObject("show server_version_num", String.class));
    assertThat(version).isBetween(180000, 189999);
    assertThat(jdbc.queryForObject("select 1", Integer.class)).isEqualTo(1);
    assertThat(flyway.validateWithResult().validationSuccessful).isTrue();
    var response = http.getForEntity("/health", String.class);
    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody()).isEqualTo("{\"status\":\"ok\"}");
    assertThat(http.getForEntity("/api/v1/unimplemented", String.class).getStatusCode().value()).isEqualTo(401);
    assertThat(context.getBeansOfType(UserDetailsService.class)).isEmpty();
    var docs = http.getForEntity("/v3/api-docs", String.class);
    assertThat(docs.getStatusCode().value()).isEqualTo(200);
    assertThat(docs.getBody()).contains("/health").doesNotContain("/__test/");
    assertThat(http.getForEntity("/swagger-ui/index.html", String.class).getStatusCode().value()).isEqualTo(200);
  }
}
