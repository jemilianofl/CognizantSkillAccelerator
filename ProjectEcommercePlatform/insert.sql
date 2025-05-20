-- Users
INSERT INTO Users (username, email, password_hash, role) VALUES
('john_doe', 'john.doe@example.com', 'hashed_password1', 'customer'),
('jane_smith', 'jane.smith@example.com', 'hashed_password2', 'customer'),
('admin_user', 'admin@example.com', 'hashed_password_admin', 'admin'),
('sam_wilson', 'sam.wilson@example.com', 'hashed_password3', 'customer');

-- Products
INSERT INTO Products (product_name, category, price, stock_quantity, description) VALUES
('Laptop Pro 15', 'Electronics', 1200.00, 50, 'High-performance laptop for professionals.'),
('Wireless Mouse', 'Electronics', 25.00, 200, 'Ergonomic wireless mouse with long battery life.'),
('SQL for Beginners', 'Books', 30.00, 100, 'A comprehensive guide to SQL.'),
('Coffee Maker Deluxe', 'Home Appliances', 75.00, 75, 'Brews up to 12 cups of coffee.'),
('Smartphone X', 'Electronics', 800.00, 30, 'Latest generation smartphone with AI camera.');

-- Orders (assuming users 1 and 2 place orders)
-- Order 1 by User 1
INSERT INTO Orders (user_id, order_status, shipping_address) VALUES
(1, 'delivered', '123 Main St, Anytown, USA');
SET @order1_id = LAST_INSERT_ID();

-- Order 2 by User 2
INSERT INTO Orders (user_id, order_status, shipping_address) VALUES
(2, 'shipped', '456 Oak Ave, Otherville, USA');
SET @order2_id = LAST_INSERT_ID();

-- Order 3 by User 1 (pending)
INSERT INTO Orders (user_id, shipping_address) VALUES
(1, '789 Pine Rd, Anytown, USA');
SET @order3_id = LAST_INSERT_ID();

-- OrderDetails
-- Order 1 Details
INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price) VALUES
(@order1_id, 1, 1, 1200.00), -- Laptop Pro 15
(@order1_id, 2, 2, 25.00);   -- Wireless Mouse (2)

-- Order 2 Details
INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price) VALUES
(@order2_id, 3, 1, 30.00),   -- SQL for Beginners
(@order2_id, 5, 1, 800.00);  -- Smartphone X

-- Order 3 Details
INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price) VALUES
(@order3_id, 4, 1, 75.00);   -- Coffee Maker Deluxe

-- Update total_amount in Orders table (ideally done by a trigger or application logic)
UPDATE Orders SET total_amount = (1 * 1200.00 + 2 * 25.00) WHERE order_id = @order1_id;
UPDATE Orders SET total_amount = (1 * 30.00 + 1 * 800.00) WHERE order_id = @order2_id;
UPDATE Orders SET total_amount = (1 * 75.00) WHERE order_id = @order3_id;


-- Payments
INSERT INTO Payments (order_id, payment_method, amount, transaction_id, status) VALUES
(@order1_id, 'credit_card', (SELECT total_amount FROM Orders WHERE order_id = @order1_id), 'txn_abc123', 'completed'),
(@order2_id, 'paypal', (SELECT total_amount FROM Orders WHERE order_id = @order2_id), 'txn_def456', 'completed');
-- No payment for order3 yet as it's pending

-- Reviews
INSERT INTO Reviews (product_id, user_id, rating, review_text) VALUES
(1, 1, 5, 'Absolutely love this laptop! Super fast and reliable.'),
(2, 1, 4, 'Good mouse, comfortable to use.'),
(3, 2, 5, 'Excellent book for learning SQL. Highly recommended.'),
(5, 2, 4, 'Great phone, camera is amazing but battery could be better.');