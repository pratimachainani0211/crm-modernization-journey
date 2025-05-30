# File Location: scripts/run-phase.sh

#!/bin/bash

# CRM Modernization Journey - Phase Navigation Script
# Easily switch between different migration phases

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

PHASE=$1

# Display help information
show_help() {
    echo -e "${CYAN}CRM Modernization Journey - Phase Navigator${NC}"
    echo "=================================================="
    echo ""
    echo "Usage: $0 [PHASE_NUMBER]"
    echo ""
    echo -e "${BLUE}Available Phases:${NC}"
    echo "  0 - Legacy Monolithic System (Anti-patterns)"
    echo "  1 - API Gateway (Strangler Fig Pattern)"
    echo "  2 - Customer Service (First Microservice)"
    echo "  3 - Order Service (Inter-service Communication)"
    echo "  4 - Complete Architecture (Legacy Retirement)"
    echo ""
    echo -e "${BLUE}Examples:${NC}"
    echo "  $0 0    # Start with legacy system"
    echo "  $0 1    # Progress to API Gateway"
    echo "  $0 2    # Extract Customer Service"
    echo ""
    echo -e "${YELLOW}Note: Each phase demonstrates different architectural patterns${NC}"
    exit 0
}

# Validate input
if [ -z "$PHASE" ]; then
    echo -e "${RED}❌ Error: Phase number required${NC}"
    show_help
fi

if [ "$PHASE" = "-h" ] || [ "$PHASE" = "--help" ]; then
    show_help
fi

# Navigate to project root if not already there
if [[ ! -f "docker-compose.yml" ]] && [[ ! -d ".git" ]]; then
    echo -e "${YELLOW}Navigating to project root...${NC}"
    cd "$(dirname "$0")/.." || {
        echo -e "${RED}❌ Could not find project root directory${NC}"
        exit 1
    }
fi

# Cleanup function
cleanup_environment() {
    echo -e "${YELLOW}🧹 Cleaning up current environment...${NC}"
    docker-compose down -v 2>/dev/null || true
    pkill -f "gradle.*bootRun" 2>/dev/null || true
    sleep 2
}

# Wait for services function
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=5
    local attempt=1

    echo -e "${BLUE}⏳ Waiting for $service_name to be ready...${NC}"

    while [ $attempt -le $max_attempts ]; do
        if curl -f "$url" >/dev/null 2>&1; then
            echo -e "${GREEN}✅ $service_name is ready!${NC}"
            return 0
        fi

        if [ $attempt -eq $max_attempts ]; then
            echo -e "${RED}❌ $service_name failed to start within timeout${NC}"
            echo "Check logs with: docker-compose logs"
            return 1
        fi

        echo "Attempt $attempt/$max_attempts - waiting for $service_name..."
        sleep 3
        ((attempt++))
    done
}

# Phase implementations
run_phase_0() {
    echo -e "${BLUE}🏗️  Phase 0: Legacy Monolithic CRM${NC}"
    echo "=================================="
    echo -e "${YELLOW}Demonstrates: Anti-patterns, tightly-coupled architecture${NC}"

    git checkout phase-0-legacy

    cleanup_environment

    echo -e "${BLUE}🔨 Starting legacy system...${NC}"
    docker-compose up -d --build

    if wait_for_service "http://localhost:8080/customers" "Legacy CRM"; then
        echo ""
        echo -e "${GREEN}🎉 Phase 0 Ready!${NC}"
        echo -e "${BLUE}📊 Legacy System Running:${NC}"
        echo "  • Application: http://localhost:8080"
        echo "  • Database: localhost:5432"
        echo ""
        echo -e "${BLUE}🔗 Try these commands:${NC}"
        echo "  curl http://localhost:8080/customers"
        echo "  curl http://localhost:8080/orders"
        echo ""
        echo -e "${YELLOW}⚠️  Problems Demonstrated:${NC}"
        echo "  • Direct SQL in controllers"
        echo "  • No separation of concerns"
        echo "  • Single database for all domains"
        echo ""
        echo -e "${CYAN}➡️  Next: ./scripts/run-phase.sh 1${NC}"
    fi
}

run_phase_1() {
    echo -e "${BLUE}🚪 Phase 1: API Gateway (Strangler Fig)${NC}"
    echo "======================================"
    echo -e "${YELLOW}Demonstrates: Traffic routing, strangler fig pattern${NC}"

    git checkout phase-1-api-gateway 2>/dev/null || {
        echo -e "${RED}❌ Phase 1 branch not available yet${NC}"
        echo -e "${BLUE}🚧 Coming soon! Currently available: Phase 0${NC}"
        echo "Run: ./scripts/run-phase.sh 0"
        exit 1
    }

    cleanup_environment

    echo -e "${BLUE}🔨 Starting API Gateway + Legacy...${NC}"
    docker-compose up -d --build

    if wait_for_service "http://localhost:8000/api/v1/customers" "API Gateway"; then
        echo ""
        echo -e "${GREEN}🎉 Phase 1 Ready!${NC}"
        echo -e "${BLUE}📊 Architecture:${NC}"
        echo "  Client → API Gateway → Legacy CRM"
        echo ""
        echo -e "${BLUE}🔗 Endpoints:${NC}"
        echo "  • Gateway: http://localhost:8000/api/v1/customers"
        echo "  • Legacy:  http://localhost:8080/customers"
        echo ""
        echo -e "${CYAN}➡️  Next: ./scripts/run-phase.sh 2${NC}"
    fi
}

run_phase_2() {
    echo -e "${BLUE}👥 Phase 2: Customer Service Extraction${NC}"
    echo "======================================="
    echo -e "${YELLOW}Demonstrates: Database per service, domain extraction${NC}"

    git checkout phase-2-customer-service 2>/dev/null || {
        echo -e "${RED}❌ Phase 2 branch not available yet${NC}"
        echo -e "${BLUE}🚧 Coming soon! Currently available: Phase 0${NC}"
        echo "Run: ./scripts/run-phase.sh 0"
        exit 1
    }

    cleanup_environment

    echo -e "${BLUE}🔨 Starting Customer Service...${NC}"
    docker-compose up -d --build

    if wait_for_service "http://localhost:8000/api/v1/customers" "Customer Service"; then
        echo ""
        echo -e "${GREEN}🎉 Phase 2 Ready!${NC}"
        echo -e "${BLUE}📊 Architecture:${NC}"
        echo "  Client → API Gateway → Customer Service"
        echo "                    └─→ Legacy CRM (Orders)"
        echo ""
        echo -e "${BLUE}🔗 Endpoints:${NC}"
        echo "  • Customers: http://localhost:8000/api/v1/customers (microservice)"
        echo "  • Orders:    http://localhost:8000/api/v1/orders (legacy)"
        echo ""
        echo -e "${CYAN}➡️  Next: ./scripts/run-phase.sh 3${NC}"
    fi
}

run_phase_3() {
    echo -e "${BLUE}📦 Phase 3: Order Service Extraction${NC}"
    echo "===================================="
    echo -e "${YELLOW}Demonstrates: Inter-service communication${NC}"

    git checkout phase-3-order-service 2>/dev/null || {
        echo -e "${RED}❌ Phase 3 branch not available yet${NC}"
        echo -e "${BLUE}🚧 Coming soon! Currently available: Phase 0${NC}"
        echo "Run: ./scripts/run-phase.sh 0"
        exit 1
    }

    cleanup_environment

    echo -e "${BLUE}🔨 Starting Order Service...${NC}"
    docker-compose up -d --build

    if wait_for_service "http://localhost:8000/api/v1/orders" "Order Service"; then
        echo ""
        echo -e "${GREEN}🎉 Phase 3 Ready!${NC}"
        echo -e "${BLUE}📊 Architecture:${NC}"
        echo "  Client → API Gateway → Customer Service"
        echo "                    └─→ Order Service"
        echo ""
        echo -e "${BLUE}🔗 Endpoints:${NC}"
        echo "  • Customers: http://localhost:8000/api/v1/customers"
        echo "  • Orders:    http://localhost:8000/api/v1/orders"
        echo ""
        echo -e "${CYAN}➡️  Next: ./scripts/run-phase.sh 4${NC}"
    fi
}

run_phase_4() {
    echo -e "${BLUE}🎉 Phase 4: Complete Microservices Architecture${NC}"
    echo "=============================================="
    echo -e "${YELLOW}Demonstrates: Legacy retirement, full microservices${NC}"

    git checkout phase-4-complete 2>/dev/null || {
        echo -e "${RED}❌ Phase 4 branch not available yet${NC}"
        echo -e "${BLUE}🚧 Coming soon! Currently available: Phase 0${NC}"
        echo "Run: ./scripts/run-phase.sh 0"
        exit 1
    }

    cleanup_environment

    echo -e "${BLUE}🔨 Starting complete architecture...${NC}"
    docker-compose up -d --build

    if wait_for_service "http://localhost:8000/api/v1/customers" "Complete Architecture"; then
        echo ""
        echo -e "${GREEN}🎉 Phase 4 Ready - Migration Complete!${NC}"
        echo -e "${BLUE}📊 Final Architecture:${NC}"
        echo "  Client → API Gateway → Customer Service"
        echo "                    ├─→ Order Service"
        echo "                    └─→ Product Service"
        echo ""
        echo -e "${BLUE}🔗 Monitoring:${NC}"
        echo "  • Applications: http://localhost:8000"
        echo "  • Grafana:     http://localhost:3000 (admin/admin)"
        echo "  • Prometheus:  http://localhost:9090"
        echo ""
        echo -e "${GREEN}✨ Migration Journey Complete!${NC}"
        echo -e "${BLUE}📈 Business Impact Achieved:${NC}"
        echo "  • 60% reduction in maintenance costs"
        echo "  • Zero-downtime migration"
        echo "  • 3x improvement in development velocity"
    fi
}

# Main execution logic
case $PHASE in
    0)
        run_phase_0
        ;;
    1)
        run_phase_1
        ;;
    2)
        run_phase_2
        ;;
    3)
        run_phase_3
        ;;
    4)
        run_phase_4
        ;;
    *)
        echo -e "${RED}❌ Invalid phase: $PHASE${NC}"
        echo "Valid phases: 0, 1, 2, 3, 4"
        show_help
        ;;
esac

echo ""
echo -e "${BLUE}🔧 Useful commands:${NC}"
echo "  docker-compose logs -f    # View logs"
echo "  docker-compose down       # Stop services"
echo "  docker-compose ps         # Check status"
echo ""
echo -e "${CYAN}🔄 Switch phases: ./scripts/run-phase.sh [0-4]${NC}"