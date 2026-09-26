# Product Requirements Document

## Document Status

| Field | Value |
| --- | --- |
| Product | BlueMoon Apartment Management and Fee Collection System |
| Status | Draft — source requirements captured, open questions remain |
| Owner | TBD |
| Last reviewed | 2026-09-26 |
| Primary source | Course assignment: BlueMoon apartment fee-management problem statement |

## 1. Product Summary

BlueMoon apartment building currently manages fee collection manually, with some support from Excel. The Management Board needs a web-based system to improve the management of apartment fees, household payments, apartment information, and resident information.

Version 1.0 focuses on fee/contribution management, household fee collection, lookup/search, basic statistics, apartment information, resident information, population changes, temporary absence, temporary residence, authentication, and account self-management for the Management Board.

Version 2.0 is planned to extend fee management with parking fees and utility/service charges collected on behalf of external providers.

## 2. Background And Problem

BlueMoon is a 30-floor apartment building. Residents or apartment owners periodically pay costs used for operation and routine maintenance. Fee-management and collection activities are handled by the elected Management Board.

The current process is manual and uses tools such as Excel, but the Management Board considers the current management effectiveness insufficient. The requested product is therefore a software system for managing apartment fee collection and related resident/apartment information.

## 3. Target Users

| User group | Context | Primary need |
| --- | --- | --- |
| Management Board (`Ban quản trị`) | Staff responsible for apartment administration and fee collection | Manage fees, household payments, apartments, residents, population information, searches, statistics, and their own account |

### 3.1 Non-System Stakeholders

The source mentions local authorities / competent authorities as recipients of resident and population information when requested. The source does **not** state that those authorities directly use the software, so they are not treated as system users in v1.0.

Households, residents, and apartment owners are subjects of managed data and fee collection. The source does **not** state that they directly log in to the v1.0 system.

## 4. Product Goals

- Replace or reduce manual fee-management work performed with paper records and spreadsheets.
- Manage fee/contribution information centrally.
- Track household fee collection.
- Support lookup, search, and basic collection statistics.
- Maintain basic apartment and resident information needed by the Management Board.
- Maintain population-related information including population changes, temporary absence, and temporary residence.
- Restrict management functions to authenticated Management Board users.

## 5. Release Scope

### 5.1 Version 1.0 — In Scope

- Account registration flow, subject to the clarification in `Open Questions`.
- Login for Management Board users.
- Management of the logged-in user's personal information.
- Password change.
- Management of fee/contribution information.
- Collection and tracking of household fees.
- Lookup and search.
- Basic collection statistics.
- Management of apartment information.
- Management of resident information.
- Management of household population information.
- Management of population changes.
- Temporary absence information.
- Temporary residence information.

### 5.2 Version 2.0 — Explicitly Out Of Scope For v1.0

- Monthly parking-fee management based on registered resident vehicles.
- Monthly motorcycle parking fee.
- Monthly car parking fee.
- Electricity-charge collection on behalf of the service provider.
- Water-charge collection on behalf of the service provider.
- Internet-charge collection on behalf of the service provider.

### 5.3 Other Non-Goals For v1.0

The source does not request the following, so they must not be assumed as v1.0 requirements without an approved requirement change:

- Resident self-service portal.
- Online payment gateway integration.
- Mobile application.
- Direct integration with government systems.
- Direct integration with electricity, water, or internet providers.
- Features outside the BlueMoon management and fee-collection scope.

## 6. Fee Types And Business Rules

### 6.1 Apartment Service Fee

- Mandatory.
- Collected monthly.
- Used for shared-area cleaning and maintenance, landscaping, waste collection, garden maintenance, security, and similar shared services.
- Calculated based on the owned apartment area.
- The source states a current range of **2,500 to 16,500 VND/m²/month**.

### 6.2 Apartment Management Fee

- Mandatory.
- Collected monthly.
- Used for apartment-management and operation activities.
- Depends on the building/project standard and quality.
- For BlueMoon, the source states the management fee is **from 7,000 VND/m²**.
- The exact rate/configuration rule used by the software is not fully specified and remains to be clarified.

### 6.3 Voluntary Contributions

- Collected in campaigns/periods rather than necessarily every month.
- Coordinated by the Management Board with local authorities or neighborhood organizations.
- Examples include funds for the poor, sea/island funds, charity funds, and similar contributions.
- Not mandatory.
- Payment is voluntary.

### 6.4 Version 2.0 Parking And Utility Rules

- Parking fees are collected monthly based on registered resident vehicles.
- Motorcycle parking fee: **70,000 VND/vehicle/month**.
- The source text states the car parking fee as **"1.200.000 nghìn đồng/xe/một tháng"**. This wording is ambiguous and must be clarified before implementation; this PRD intentionally does not silently correct it.
- Electricity, water, and internet charges are collected monthly according to notices from the corresponding service providers.

## 7. User Stories

Priority uses `Must`, `Should`, `Could`, and `Won't` relative to **v1.0**.

| ID | Story | Priority | Acceptance criteria |
| --- | --- | --- | --- |
| US-AUTH-001 | As a Management Board user, I want to log in so that I can access protected management functions. | Must | A valid Management Board account can authenticate; protected v1.0 management functions are unavailable before successful login. |
| US-AUTH-002 | As a Management Board user, I want to change my password so that I can maintain my account security. | Must | An authenticated user can change the password for their own account. |
| US-AUTH-003 | As a Management Board user, I want to manage my personal information so that my account information remains current. | Must | An authenticated user can view and maintain the personal information that the system stores for their account. Exact editable fields are TBD. |
| US-AUTH-004 | As a user, I want an account-registration flow so that an account can be created. | Must | A registration flow exists, but who may register and how registration relates to “provided accounts” must be clarified before implementation. |
| US-FEE-001 | As a Management Board user, I want to manage fee and contribution information so that the system represents the amounts households may need to pay. | Must | The system can maintain fee/contribution records for the v1.0 fee types. Exact CRUD semantics are TBD. |
| US-FEE-002 | As a Management Board user, I want mandatory monthly fees to reflect the source fee rules so that household obligations can be determined. | Must | Service fees can use apartment area and a configured rate; management fees can use apartment area and a configured BlueMoon management-fee rate. |
| US-FEE-003 | As a Management Board user, I want voluntary contributions to remain optional so that households are not treated as obligatorily owing them. | Must | Contribution records can be identified as voluntary and campaign/period-based. |
| US-PAY-001 | As a Management Board user, I want to record household fee collection so that I can track what has been paid. | Must | The system can record collection/payment information associated with a household and a fee/contribution. Exact payment fields and partial-payment rules are TBD. |
| US-APT-001 | As a Management Board user, I want to manage apartment information so that apartment data is available for administration and fee calculation. | Must | Apartment information can be stored and maintained. The exact set of fields and allowed CRUD operations are TBD. |
| US-RES-001 | As a Management Board user, I want to manage resident information so that resident and household population data is available when required. | Must | Resident information can be stored and maintained and associated with the relevant household/apartment model once that model is approved. |
| US-POP-001 | As a Management Board user, I want to manage household population information and population changes so that current population records can be maintained. | Must | The system can record household population information and population-change activities described by the source. Exact change types are TBD. |
| US-POP-002 | As a Management Board user, I want to record temporary absence information so that temporary absence can be tracked. | Must | Temporary-absence information can be stored and retrieved. Exact fields and lifecycle are TBD. |
| US-POP-003 | As a Management Board user, I want to record temporary residence information so that temporary residence can be tracked. | Must | Temporary-residence information can be stored and retrieved. Exact fields and lifecycle are TBD. |
| US-SEARCH-001 | As a Management Board user, I want lookup and search so that I can find relevant management information efficiently. | Must | Search/lookup is available for relevant v1.0 management data. Exact searchable fields and filters are TBD. |
| US-STAT-001 | As a Management Board user, I want basic fee-collection statistics so that I can understand the current collection status. | Must | The system presents basic statistics about collected fees/contributions. Exact metrics are TBD. |
| US-V2-001 | As a Management Board user, I want to manage monthly parking fees based on registered vehicles. | Won't | Deferred to v2.0. |
| US-V2-002 | As a Management Board user, I want to manage electricity, water, and internet charges collected on behalf of providers. | Won't | Deferred to v2.0. |

## 8. Functional Requirements

### 8.1 Authentication And Account

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-AUTH-001 | The system shall require successful login before the Management Board can access the protected apartment, resident, population, fee, payment, search, statistics, and account-management functions. | US-AUTH-001 | Draft |
| FR-AUTH-002 | The system shall allow an authenticated Management Board user to change their password. | US-AUTH-002 | Draft |
| FR-AUTH-003 | The system shall allow an authenticated Management Board user to manage the personal information stored for their own account. | US-AUTH-003 | Draft |
| FR-AUTH-004 | The system shall provide an account-registration business flow. The registration policy is TBD because the source also states that Management Board users log in with provided accounts. | US-AUTH-004 | Blocked by clarification |

### 8.2 Fee And Contribution Management

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-FEE-001 | The system shall manage fee/contribution information used by the Management Board. | US-FEE-001 | Draft |
| FR-FEE-002 | The system shall support mandatory monthly apartment service fees calculated from apartment area and a configured service-fee rate. | US-FEE-002 | Draft |
| FR-FEE-003 | The system shall support mandatory monthly apartment management fees based on apartment area and a configured management-fee rate. | US-FEE-002 | Draft |
| FR-FEE-004 | The system shall support voluntary contribution campaigns that are not treated as mandatory household fees. | US-FEE-003 | Draft |
| FR-FEE-005 | The system shall support the source-stated service-fee range of 2,500–16,500 VND/m²/month as business data/configuration rather than hard-coding a single rate. | US-FEE-002 | Draft |
| FR-FEE-006 | The exact BlueMoon management-fee configuration shall remain configurable until the stakeholder confirms the intended rate rule. | US-FEE-002 | Draft |

### 8.3 Household Fee Collection

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-PAY-001 | The system shall record fee/contribution collection for households. | US-PAY-001 | Draft |
| FR-PAY-002 | Collection records shall identify the relevant household and fee/contribution. | US-PAY-001 | Draft |
| FR-PAY-003 | Rules for partial payments, overpayments, reversals, receipts, and payment methods are not specified by the source and shall not be invented without approval. | US-PAY-001 | Requires clarification before such behavior is implemented |

### 8.4 Apartment Management

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-APT-001 | The system shall maintain basic apartment information for BlueMoon. | US-APT-001 | Draft |
| FR-APT-002 | Apartment data shall support area information because mandatory fee calculations depend on apartment area. | US-APT-001, US-FEE-002 | Draft |
| FR-APT-003 | The exact apartment fields and CRUD operations are not defined by the source and shall be approved before implementation of destructive operations. | US-APT-001 | Requires clarification |

### 8.5 Resident And Population Management

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-RES-001 | The system shall maintain basic resident information. | US-RES-001 | Draft |
| FR-RES-002 | The system shall maintain household population information. | US-POP-001 | Draft |
| FR-RES-003 | The system shall maintain population-change activity information. | US-POP-001 | Draft |
| FR-RES-004 | The system shall maintain temporary-absence information. | US-POP-002 | Draft |
| FR-RES-005 | The system shall maintain temporary-residence information. | US-POP-003 | Draft |
| FR-RES-006 | The system shall allow the Management Board to retrieve resident/population information when it is needed for provision to competent authorities. | US-RES-001, US-SEARCH-001 | Draft |

### 8.6 Search And Statistics

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-SEARCH-001 | The system shall provide lookup and search for v1.0 management information. | US-SEARCH-001 | Draft |
| FR-SEARCH-002 | Searchable resources, fields, filters, sorting, and pagination behavior are TBD. | US-SEARCH-001 | Requires clarification |
| FR-STAT-001 | The system shall provide basic statistics that help the Management Board understand the current fee/contribution collection status. | US-STAT-001 | Draft |
| FR-STAT-002 | Exact statistical indicators, grouping periods, charts, and export behavior are TBD. | US-STAT-001 | Requires clarification |

### 8.7 Version 2.0 Deferred Requirements

| ID | Requirement | Related story | Status |
| --- | --- | --- | --- |
| FR-V2-001 | The system shall manage monthly parking fees based on registered resident vehicles. | US-V2-001 | Deferred to v2.0 |
| FR-V2-002 | The motorcycle parking fee shall support the source-stated amount of 70,000 VND/vehicle/month. | US-V2-001 | Deferred to v2.0 |
| FR-V2-003 | The car parking fee value must be clarified because the source wording is ambiguous. | US-V2-001 | Blocked / deferred |
| FR-V2-004 | The system shall manage monthly electricity, water, and internet charges according to notices from the respective providers. | US-V2-002 | Deferred to v2.0 |

## 9. Primary Business Flows

The assignment explicitly identifies these business flows:

1. Account registration.
2. Create a fee/collection item.
3. Collect fees.
4. View statistics for contributions/collections.

Detailed use-case flows, alternate flows, validation rules, and permissions are not included in the source and should be specified before implementation of each flow.

## 10. Non-Functional Requirements

Only requirements supported by the source or accepted project decisions are stated as requirements. Other categories remain TBD instead of being invented.

| ID | Category | Requirement | Verification |
| --- | --- | --- | --- |
| NFR-SEC-001 | Access control | Protected v1.0 management functions shall be accessible only after successful Management Board login. | Attempt protected operations without authentication and verify access is denied. |
| NFR-TECH-001 | Delivery form | The product shall be a web application. | Verify the delivered system is accessible through a web frontend and backend API. |
| NFR-DATA-001 | Persistence | Application data shall use the project-approved centralized PostgreSQL database. | Verify application persistence configuration and integration tests against PostgreSQL. |
| NFR-PERF-001 | Performance | TBD — the source defines no response-time, throughput, or concurrency target. | TBD |
| NFR-ACC-001 | Accessibility | TBD — the source defines no accessibility standard or target. | TBD |
| NFR-REL-001 | Reliability | TBD — the source defines no availability, backup, or recovery target. | TBD |

## 11. Project Constraints

### 11.1 Constraints From The Assignment

- The system is intended to be delivered as a web application.
- The source originally proposes Java (Spring Boot), ReactJS, and a centralized MySQL Server database.

### 11.2 Accepted Repository Decisions

The repository's accepted architecture decision supersedes the source's MySQL implementation choice while preserving the required product behavior:

- Java 17.
- Spring Boot 3.x.
- React with TypeScript.
- HeroUI for the primary component library.
- PostgreSQL as the relational database.
- REST/OpenAPI for the frontend/backend contract.

Exact versions and tooling belong to `docs/tech_stack.md` and `docs/decisions.md`, not this PRD.

## 12. Success Metrics

The source does not define measurable success targets. Do not invent them before stakeholder agreement.

| Metric | Baseline | Target | Measurement method |
| --- | --- | --- | --- |
| Manual/spreadsheet management effort | Current process uses manual records and Excel | TBD | TBD |
| Fee-record accuracy | TBD | TBD | TBD |
| Search/retrieval efficiency | TBD | TBD | TBD |
| Collection-statistics usefulness | TBD | TBD | TBD |

## 13. Risks And Assumptions

| Item | Type | Impact | Response |
| --- | --- | --- | --- |
| “Manage” is not decomposed into exact CRUD operations in the source. | Requirement gap | High | Define allowed operations per resource before coding destructive actions. |
| The source lists account registration but also states that Management Board users log in with accounts that have been provided. | Requirement conflict / ambiguity | High | Clarify account-provisioning and registration policy before implementing registration. |
| Exact data fields for apartments, residents, population changes, temporary absence, and temporary residence are not specified. | Requirement gap | High | Define domain fields before database and API design are finalized. |
| Exact statistics are not specified. | Requirement gap | Medium | Agree on required metrics before implementing the statistics module. |
| Search fields and filtering behavior are not specified. | Requirement gap | Medium | Define per-resource search behavior in the API contract. |
| Partial-payment, overpayment, refund/reversal, receipt, and payment-method behavior are not specified. | Requirement gap | High | Keep these out of scope until explicitly approved. |
| The v2.0 car parking fee text is ambiguous. | Source ambiguity | Medium | Confirm the intended numeric amount before implementing v2.0 parking fees. |
| The source uses MySQL while the team has selected PostgreSQL. | Implementation divergence | Low | Treat PostgreSQL as an accepted technical decision; preserve product behavior. |

## 14. Open Questions

These questions must be answered before the corresponding implementation is considered stable:

1. **Account registration:** Is registration public, Management-Board-only, invitation-based, or performed by an administrator? How does it relate to the statement that login accounts are already provided?
2. **Roles:** Is there only one Management Board role in v1.0, or are multiple permission levels required?
3. **Apartment fields:** Which fields are mandatory beyond apartment area? Examples are intentionally not assumed here.
4. **Household model:** What is the exact relationship between apartment, household, apartment owner, and residents?
5. **Resident fields:** Which resident identity and demographic fields must be stored?
6. **Population changes:** Which change types must be represented and what data must each record contain?
7. **Temporary absence/residence:** What fields, dates, statuses, and lifecycle rules are required?
8. **Fee configuration:** What exact BlueMoon service-fee and management-fee rates should be configured initially?
9. **Fee lifecycle:** Can a fee/collection item be edited or deleted after household obligations/payments exist?
10. **Payment rules:** Are partial payments supported? Are overpayments, reversals, refunds, payment methods, and receipts required?
11. **Search:** Which resources and fields must be searchable, and are pagination/sorting/filtering required?
12. **Statistics:** Which statistics must be displayed in v1.0 and over which time periods?
13. **v2.0 car fee:** What exact monthly car parking fee is intended by the source wording `1.200.000 nghìn đồng/xe/một tháng`?

## 15. Requirement Interpretation Rules For Agents

- Do not silently expand the word **“manage”** into create/read/update/delete unless the specific operations have been approved.
- Do not implement v2.0 requirements as part of v1.0 unless the PRD is changed first.
- Do not invent domain fields, payment rules, statistics, permissions, or search behavior to fill requirement gaps.
- When an open question blocks a task, stop and report the missing decision.
- Product behavior changes must update this PRD before or together with implementation.
- API and database designs must trace back to an approved functional requirement in this document.
