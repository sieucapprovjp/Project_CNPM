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

## Status Codes

| Status | Use |
| --- | --- |
| `400` | Malformed request |
| `401` | Missing or invalid authentication |
| `403` | Authenticated caller lacks permission |
| `404` | Resource does not exist or must not be disclosed |
| `409` | State or uniqueness conflict |
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
