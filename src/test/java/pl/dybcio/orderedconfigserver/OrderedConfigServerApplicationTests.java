package pl.dybcio.orderedconfigserver;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class OrderedConfigServerApplicationTests {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void servesSharedJwtSecret_toAnyClientApplicationName() {
    ResponseEntity<String> response =
        restTemplate.getForEntity("/order-service/default", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("app.jwt.secret");
    assertThat(response.getBody()).contains("change-me-in-prod-min-256-bits-long-please-replace");
  }

  @Test
  void servesSharedJwtSecret_toGatewayUnderItsOwnPropertyPath() {
    ResponseEntity<String> response = restTemplate.getForEntity("/gateway/default", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("ordered.gateway.security.jwt-secret");
  }

  @Test
  void healthEndpoint_isUp() {
    ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("\"status\":\"UP\"");
  }
}
