# Phase 1: API Gateway - Strangler Fig Pattern

**Solution**: Introduce API Gateway to route traffic and begin strangling the legacy system.

## Issues Addressed
- Creates API abstraction layer over legacy system
- Establishes foundation for gradual migration
- Enables traffic routing and control
- Maintains backward compatibility during transition

## Architecture
Client → API Gateway (Port 8000) → Legacy CRM (Port 8080)

## Key Concepts Demonstrated
- **Strangler Fig Pattern**: New system gradually replaces old system
- **API Gateway**: Single entry point for all client requests
- **Traffic Routing**: Intelligent request forwarding
- **Service Isolation**: Gateway runs independently from legacy

## Quick Start
```bash
./scripts/setup.sh
curl http://localhost:8000/api/v1/customers  # Through API Gateway
curl http://localhost:8080/customers         # Direct to legacy (still works)
```

## Endpoints

API Gateway: http://localhost:8000/api/v1/customers
API Gateway: http://localhost:8000/api/v1/orders
Legacy Direct: http://localhost:8080/customers (bypassed)

## What's New in Phase 1

- ✅ API Gateway service (Spring Boot on port 8000)
- ✅ Routing logic to legacy system
- ✅ Versioned API endpoints (/api/v1/*)
- ✅ Independent containerized service
- ✅ Health check endpoints

## Migration Benefits

- **Zero Risk**: Legacy system unchanged, fully functional
- **Gradual Transition**: Can route specific endpoints to new services
- **Client Compatibility**: New API structure without breaking changes
- **Monitoring**: Centralized logging and request tracking

**Next:** See **phase-2-customer-service** for first domain extraction.