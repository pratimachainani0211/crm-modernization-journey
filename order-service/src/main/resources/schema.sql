-- Order Service Database Schema
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL, -- Reference to customer service
    total DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
    product_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data for order service
INSERT INTO orders (customer_id, total, status) VALUES
(1, 299.99, 'COMPLETED'),
(2, 599.99, 'PENDING'),
(1, 199.99, 'SHIPPED')
ON CONFLICT DO NOTHING;

INSERT INTO order_items (order_id, product_name, quantity, unit_price) VALUES
(1, 'Laptop', 1, 299.99),
(2, 'Phone', 1, 599.99),
(3, 'Headphones', 1, 199.99)
ON CONFLICT DO NOTHING;