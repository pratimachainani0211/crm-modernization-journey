**Solution**: Extract customer domain into dedicated microservice with its own database.

## Architecture
```
Client → API Gateway → Customer Service (NEW)
                  └─→ Legacy CRM (Orders)
```

## Key Concepts
- Database per service
- Domain extraction
- Service isolation

## Quick Start
```bash
docker-compose up -d
curl http://localhost:8000/api/v1/customers  # Now routed to customer service
curl http://localhost:8000/api/v1/orders     # Still routed to legacy

**Next**: See `phase-3-order-service` for order service extraction.