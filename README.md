# CRM Modernization Journey

Legacy monolith to microservices transformation using Strangler Fig pattern.

<<<<<<< HEAD
## Phases

| Phase | Description | Branch | Status |
|-------|-------------|--------|---------|
| **Phase 0** | Legacy Monolith | [`phase-0-legacy`](../../tree/phase-0-legacy) | ✅ Complete |
| **Phase 1** | API Gateway | `phase-1-api-gateway` | 🚧 Next |
| **Phase 2** | Customer Service | `phase-2-customer-service` | ⏳ Planned |
| **Phase 3** | Order Service | `phase-3-order-service` | ⏳ Planned |

## Quick Start

```bash
# Explore Phase 0 (Legacy System)
=======
## Architecture Evolution

| Phase | Description | Branch | Status |
|-------|-------------|--------|--------|
| **Phase 0** | Legacy Monolith | [`phase-0-legacy`](../../tree/phase-0-legacy) | ✅ Complete |
| **Phase 1** | API Gateway | `phase-1-api-gateway` | 🚧 Coming Next |
| **Phase 2** | Customer Service | `phase-2-customer-service` | ⏳ Planned |
| **Phase 3** | Order Service | `phase-3-order-service` | ⏳ Planned |

## Current Phase: Phase 0 - Legacy System

**Problems Demonstrated:**
- Direct SQL queries in controllers
- No separation of concerns
- Single database for all domains
- Tightly coupled architecture

**Quick Start:**
```bash
>>>>>>> 31f2db1 ([Pratima] Setting up initial README)
git checkout phase-0-legacy
./scripts/setup.sh
curl http://localhost:8080/customers