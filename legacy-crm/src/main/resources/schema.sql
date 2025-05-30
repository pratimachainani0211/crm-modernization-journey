-- Legacy CRM Database Schema (Reduced - customers and orders moved to microservices)
-- Only keeping products and other non-extracted domains

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample product data
INSERT INTO products (name, description, price, stock_quantity) VALUES
('Laptop', 'High-performance laptop', 299.99, 10),
('Phone', 'Smartphone with great camera', 599.99, 25),
('Headphones', 'Noise-cancelling headphones', 199.99, 15)
ON CONFLICT DO NOTHING;