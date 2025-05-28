#!/bin/bash

# scripts/setup.sh - Phase 0 Legacy Setup Script

set -e  # Exit on any error

echo "🏗️  Setting up Phase 0: Legacy Monolithic CRM"
echo "=============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Docker is running
check_docker() {
    echo -e "${BLUE}Checking Docker...${NC}"
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker and try again.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker is running${NC}"
}

# Check if Docker Compose is available
check_docker_compose() {
    echo -e "${BLUE}Checking Docker Compose...${NC}"
    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        echo -e "${RED}❌ Docker Compose is not available. Please install Docker Compose.${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker Compose is available${NC}"
}

# Clean up any existing containers
cleanup() {
    echo -e "${YELLOW}🧹 Cleaning up existing containers...${NC}"
    docker-compose down -v 2>/dev/null || true
    docker system prune -f 2>/dev/null || true
    echo -e "${GREEN}✅ Cleanup completed${NC}"
}

# Build and start services
build_and_start() {
    echo -e "${BLUE}🔨 Building and starting Legacy CRM services...${NC}"
    echo "This may take a few minutes on first run..."

    # Build with no cache to ensure fresh build
    docker-compose build --no-cache

    # Start services in detached mode
    docker-compose up -d

    echo -e "${GREEN}✅ Services started${NC}"
}

# Wait for services to be healthy
wait_for_services() {
    echo -e "${BLUE}⏳ Waiting for services to be ready...${NC}"

    # Wait for database
    echo "Waiting for PostgreSQL database..."
    sleep 10

    # Wait for application (with timeout)
    echo "Waiting for Legacy CRM application..."

    max_attempts=30
    attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:8080/customers > /dev/null 2>&1; then
            echo -e "${GREEN}✅ Legacy CRM is ready!${NC}"
            break
        fi

        if [ $attempt -eq $max_attempts ]; then
            echo -e "${RED}❌ Legacy CRM failed to start within timeout${NC}"
            echo "Check logs with: docker-compose logs legacy-app"
            exit 1
        fi

        echo "Attempt $attempt/$max_attempts - still waiting..."
        sleep 5
        ((attempt++))
    done
}

# Test the application
test_application() {
    echo -e "${BLUE}🧪 Testing Legacy CRM application...${NC}"

    # Test customers endpoint
    echo "Testing customers endpoint..."
    if curl -f http://localhost:8080/customers > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Customers endpoint working${NC}"
    else
        echo -e "${RED}❌ Customers endpoint failed${NC}"
        exit 1
    fi

    # Test orders endpoint
    echo "Testing orders endpoint..."
    if curl -f http://localhost:8080/orders > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Orders endpoint working${NC}"
    else
        echo -e "${RED}❌ Orders endpoint failed${NC}"
        exit 1
    fi
}

# Display success information
display_success() {
    echo ""
    echo -e "${GREEN}🎉 Phase 0 Legacy CRM Setup Complete!${NC}"
    echo "========================================="
    echo ""
    echo -e "${BLUE}📊 Available Services:${NC}"
    echo "  • Legacy CRM Application: http://localhost:8080"
    echo "  • PostgreSQL Database:    localhost:5432"
    echo ""
    echo -e "${BLUE}🔗 API Endpoints:${NC}"
    echo "  • GET  http://localhost:8080/customers"
    echo "  • POST http://localhost:8080/customers"
    echo "  • GET  http://localhost:8080/orders"
    echo ""
    echo -e "${BLUE}📝 Try these commands:${NC}"
    echo "  curl http://localhost:8080/customers"
    echo "  curl -X POST http://localhost:8080/customers \\"
    echo "    -H 'Content-Type: application/json' \\"
    echo "    -d '{\"name\":\"Test User\",\"email\":\"test@example.com\"}'"
    echo ""
    echo -e "${BLUE}🔧 Useful Docker commands:${NC}"
    echo "  docker-compose logs -f           # View logs"
    echo "  docker-compose down              # Stop services"
    echo "  docker-compose ps                # Check status"
    echo ""
    echo -e "${YELLOW}⚠️  Legacy Problems Demonstrated:${NC}"
    echo "  • Direct SQL queries in controllers"
    echo "  • No separation of concerns"
    echo "  • Single database for all domains"
    echo "  • Tightly coupled architecture"
    echo ""
    echo -e "${BLUE}➡️  Next Phase:${NC}"
    echo "  git checkout phase-1-api-gateway"
    echo "  ./scripts/setup.sh"
}

# Handle script interruption
cleanup_on_exit() {
    echo -e "\n${YELLOW}Script interrupted. Cleaning up...${NC}"
    docker-compose down 2>/dev/null || true
    exit 1
}

# Trap Ctrl+C
trap cleanup_on_exit INT

# Main execution
main() {
    # Navigate to project root if not already there
    if [[ ! -f "docker-compose.yml" ]]; then
        echo -e "${YELLOW}Navigating to project root...${NC}"
        cd "$(dirname "$0")/.." || exit 1
    fi

    check_docker
    check_docker_compose
    cleanup
    build_and_start
    wait_for_services
    test_application
    display_success
}

# Help function
show_help() {
    echo "Phase 0 Legacy CRM Setup Script"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -h, --help     Show this help message"
    echo "  --no-cleanup   Skip cleanup step"
    echo "  --no-test      Skip application testing"
    echo ""
    echo "This script will:"
    echo "  1. Check Docker and Docker Compose"
    echo "  2. Clean up existing containers"
    echo "  3. Build and start Legacy CRM services"
    echo "  4. Wait for services to be ready"
    echo "  5. Test the application endpoints"
    echo ""
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        --no-cleanup)
            SKIP_CLEANUP=true
            shift
            ;;
        --no-test)
            SKIP_TEST=true
            shift
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            show_help
            exit 1
            ;;
    esac
done

# Run main function
main

echo -e "${GREEN}✨ Setup completed successfully!${NC}"