# CRM Modernization Journey

Legacy monolith to microservices transformation using Strangler Fig pattern.

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
git checkout phase-0-legacy
./scripts/setup.sh
curl http://localhost:8080/customers