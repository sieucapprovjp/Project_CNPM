package vn.bluemoon.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "System")
public class HealthController {
  @GetMapping("/health")
  @Operation(operationId = "getHealth", summary = "Check service health")
  public HealthResponse health() {
    return new HealthResponse("ok");
  }

  public record HealthResponse(String status) {}
}
