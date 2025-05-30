-- Customer Service Database Schema
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample data for customer service
INSERT INTO customers (name, email) VALUES
('John Doe', 'john@example.com'),
('Jane Smith', 'jane@example.com'),
('Bob Johnson', 'bob@example.com')
ON CONFLICT (email) DO NOTHING;