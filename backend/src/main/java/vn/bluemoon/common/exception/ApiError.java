package vn.bluemoon.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.slf4j.MDC;

public record ApiError(ErrorBody error) {
  public static ApiError of(String code, String message) {
    return of(code, message, List.of());
  }

  public static ApiError of(String code, String message, List<Detail> details) {
    return new ApiError(new ErrorBody(code, message, details, MDC.get("requestId")));
  }

  public record ErrorBody(String code, String message, List<Detail> details, String requestId) {}

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record Detail(String field, String code, String message) {}
}
