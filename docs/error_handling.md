# Error Handling

## Goals

- Return actionable messages to clients.
- Keep internal and sensitive details out of responses.
- Make failures traceable in server logs.
- Use stable error codes for expected application errors.

## API Error Shape

```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "The requested resource was not found.",
    "details": [],
    "requestId": "request-id"
  }
}
```

`details` is optional and must contain only safe, client-actionable information.

When supplied, each detail is an object with required string `code` and `message`, plus optional string `field`. Field paths use JSON property names, such as `name` or `items[0].name`; omit `field` for a whole-request error. Never include a rejected password, token, or sensitive value. The schema is `ErrorDetail` in OpenAPI.

Use `VALIDATION_FAILED` for semantically invalid input (`422`), `MALFORMED_REQUEST` for parsing/type failures (`400`), and `INTERNAL_ERROR` for unexpected failures (`500`). Feature-specific codes use `UPPER_SNAKE_CASE` and are documented on their operation. Frontend logic uses codes/statuses rather than matching human-readable messages.

`requestId` remains optional in the shared schema. The base error handler should generate a correlation ID and include it in error responses and corresponding logs. Security-filter errors should use the same error shape as controller errors.

## Status Codes

| Status | Use |
| --- | --- |
| `400` | Malformed request |
| `401` | Missing or invalid authentication |
| `403` | Authenticated caller lacks permission |
| `404` | Resource does not exist or must not be disclosed |
| `405` | HTTP method is not supported by the resource |
| `409` | State or uniqueness conflict |
| `415` | Unsupported request media type |
| `422` | Semantically invalid input |
| `429` | Rate limit exceeded |
| `500` | Unexpected server failure |

## Logging

- Attach a request or correlation ID to server errors.
- Log the original exception on the server at the appropriate level.
- Never log passwords, tokens, session identifiers, or sensitive personal data.
- Do not expose stack traces outside local development.

## Frontend Behavior

- Show field-level messages for validation errors.
- Show a recoverable page or notification for network and server errors.
- Preserve user input when retrying is safe.
- Do not infer authorization behavior from message text; use status and error codes.

## Foundation Implementation

The backend generates a UUID in `X-Request-Id` and error `requestId`, without trusting incoming IDs. The filter clears its logging context after each request. The controller advice maps body validation and method-parameter validation to `422`, malformed requests to `400`, data-integrity conflicts to `409`, and unexpected exceptions to a safe `500` response. Spring Security emits the shared error shape for `401` and `403`. Browser CORS rejection is handled by the framework before controller processing.

Validation details intentionally use safe generic text and never include rejected values. Unexpected-error logs record the request ID and exception class, without exception messages or payloads that could expose sensitive input. Features should add safe diagnostic codes when more context is needed.
