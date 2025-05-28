# Phase 0: Legacy Monolithic CRM

**Problem**: Tightly-coupled monolithic system with direct database access in controllers.

## Issues Demonstrated
- Direct SQL queries in controllers
- No separation of concerns
- Single database for all domains
- No API abstraction layer

## Quick Start
```bash
./scripts/setup.sh
curl http://localhost:8080/customers