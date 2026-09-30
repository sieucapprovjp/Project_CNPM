package vn.bluemoon.common.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<ApiError.Detail> details = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ApiError.Detail(error.getField(), "INVALID_VALUE", "The field does not satisfy its constraints."))
        .toList();
    return new ResponseEntity<>(ApiError.of("VALIDATION_FAILED", "Input validation failed.", details),
        headers, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @Override
  protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    if (ex.isForReturnValue()) {
      return handleExceptionInternal(ex, null, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    return new ResponseEntity<>(ApiError.of("VALIDATION_FAILED", "Input validation failed."),
        headers, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ApiError> handleDataConflict(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiError.of("DATA_CONFLICT", "The change conflicts with existing data."));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String code = switch (status.value()) {
      case 400 -> "MALFORMED_REQUEST";
      case 404 -> "RESOURCE_NOT_FOUND";
      case 405 -> "METHOD_NOT_ALLOWED";
      case 415 -> "UNSUPPORTED_MEDIA_TYPE";
      default -> status.is5xxServerError() ? "INTERNAL_ERROR" : "REQUEST_REJECTED";
    };
    if (status.is5xxServerError()) logFailure(ex);
    return new ResponseEntity<>(ApiError.of(code, status.is5xxServerError()
        ? "An unexpected error occurred." : "The request could not be processed."), headers, status);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ApiError> handleUnexpected(Exception ex) {
    logFailure(ex);
    return ResponseEntity.internalServerError().body(ApiError.of("INTERNAL_ERROR", "An unexpected error occurred."));
  }

  private void logFailure(Exception ex) {
    // Exception messages and rejected values may contain resident information or credentials.
    LOG.error("Request failed: requestId={}, exceptionType={}", MDC.get("requestId"), ex.getClass().getName());
  }
}
