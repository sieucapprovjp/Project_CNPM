# Use Case Model

## Status

This model is derived from [`prd.md`](prd.md), last reviewed on 2026-09-30. It includes internal account provisioning (ADR-004) and email activation (ADR-005); remaining PRD questions are still open.

The editable PlantUML source is [`use_case_diagram.puml`](use_case_diagram.puml).

## System Actor

| Actor | Description |
| --- | --- |
| Management Board (`Ban quan tri`) | Authenticated staff responsible for apartment administration, resident and population records, fee collection, search, and statistics |
| Account administrator | Internal actor authorized to issue staff accounts; initial-account bootstrap and other permissions remain to be specified |
| Email delivery service | Supporting system that delivers activation emails; provider remains to be selected |

Households, residents, apartment owners, local authorities, and utility/service-charge providers are not actors in this model. The PRD identifies them as data subjects, payers, information recipients, or contextual organizations, but does not state that they directly use the system. The email delivery service is a supporting integration, not a user with management access.

## v1.0 Use Cases

| Area | Use case | Traceability |
| --- | --- | --- |
| Account and access | Issue staff account and email activation instructions (account administrator; email service supports delivery) | US-AUTH-004, FR-AUTH-004, FR-AUTH-007 |
| Account and access | Send replacement activation link for an unactivated account (account administrator; email service supports delivery) | US-AUTH-004, FR-AUTH-009 |
| Account and access | Set password and activate account through emailed link (staff) | US-AUTH-004, FR-AUTH-005 |
| Account and access | Log in with email and password | US-AUTH-001, FR-AUTH-001, FR-AUTH-008 |
| Account and access | Manage own personal information | US-AUTH-003, FR-AUTH-003 |
| Account and access | Change password | US-AUTH-002, FR-AUTH-002 |
| Fee collection | Manage fee and contribution information | US-FEE-001 through US-FEE-003, FR-FEE-001 through FR-FEE-006 |
| Fee collection | Record household fee collection | US-PAY-001, FR-PAY-001 through FR-PAY-003 |
| Fee collection | View basic collection statistics | US-STAT-001, FR-STAT-001 through FR-STAT-002 |
| Apartment and population | Manage apartment information | US-APT-001, FR-APT-001 through FR-APT-003 |
| Apartment and population | Manage resident information | US-RES-001, FR-RES-001 and FR-RES-006 |
| Apartment and population | Manage household population information | US-POP-001, FR-RES-002 |
| Apartment and population | Record population changes | US-POP-001, FR-RES-003 |
| Apartment and population | Record temporary absence | US-POP-002, FR-RES-004 |
| Apartment and population | Record temporary residence | US-POP-003, FR-RES-005 |
| Information access | Search and look up management information | US-SEARCH-001, FR-SEARCH-001 through FR-SEARCH-002 |

## Deferred v2.0 Use Cases

| Use case | Traceability | Status |
| --- | --- | --- |
| Manage monthly parking fees | US-V2-001, FR-V2-001 through FR-V2-003 | Deferred; car fee requires clarification |
| Manage electricity, water, and internet charges | US-V2-002, FR-V2-004 | Deferred |

## Modeling Decisions

- The diagram shows user goals, not unapproved CRUD operations.
- Login is shown as a standalone use case. Authentication is a precondition for protected management functions rather than an `include` relationship repeated across the diagram.
- Registration is represented by administrator-issued staff accounts under ADR-004. Public self-registration and resident accounts are excluded. ADR-005 makes each unique staff email the login identifier and selects a single-use email link valid for 24 hours so staff can set their own password and activate the account before normal login. An administrator can issue a replacement for an unactivated account, invalidating all prior links. PRD section 9.2 defines the accepted activation criteria; email canonicalization and send-failure handling remain TBD.
- ADR-006 establishes the initial administrator through an operator-controlled, backend-configured bootstrap against an empty account store. It is deployment behavior rather than a public user goal, so the diagram does not add a public administrator-registration actor or use case. The account administrator's business permissions beyond issuing staff accounts remain TBD.
- v2.0 is visually separated and connected with dashed future associations so it cannot be mistaken for v1.0 scope.
- Fee formulas, validation rules, data fields, filters, and statistical metrics are not separate use cases because the PRD has not finalized them.

## Rendering

Open `use_case_diagram.puml` with a PlantUML-compatible editor or render it with PlantUML:

```text
plantuml docs/use_case_diagram.puml
```
