# Phase 3: Order Service Extraction

**Solution**: Extract order domain and implement inter-service communication.

## Architecture
```
Client → API Gateway → Customer Service
                  └─→ Order Service (NEW)
                         └─→ Customer Service (API call)
```

## Key Concepts
- Inter-service communication
- Service dependencies
- Distributed system patterns

## Quick Start
```bash
docker-compose up -d
curl http://localhost:8000/api/v1/customers  # Customer service
curl http://localhost:8000/api/v1/orders     # Order service (with customer data)
```
**Architecture Validation**
```bash
# Verify customer data comes from Customer Service
curl http://localhost:8000/api/v1/customers | jq

# Verify order data comes from Order Service (with customer enrichment)
curl http://localhost:8000/api/v1/orders | jq

# Check that legacy no longer serves customers/orders
curl http://localhost:8080/customers  # Should fail/redirect
curl http://localhost:8080/orders     # Should fail/redirect
```

**Next**: See `phase-4-complete` for final architecture.