-- Fixed schema.sql - Clean and working version

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    total DECIMAL(10,2) NOT NULL
);

-- Insert sample data (with conflict handling)
INSERT INTO customers (name, email) VALUES
('John Doe', 'john@example.com'),
('Jane Smith', 'jane@example.com')
ON CONFLICT (email) DO NOTHING;

INSERT INTO orders (customer_id, total) VALUES
(1, 299.99),
(2, 599.99)
ON CONFLICT DO NOTHING;