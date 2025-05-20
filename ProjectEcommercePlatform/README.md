# E-Commerce Platform Database Project 🛍️

This document provides the database schema, sample data, and SQL queries with explanations for an e-commerce platform.

---

## 1. Database Schema 🧱

The database is named `ECommercePlatform`.

```sql
CREATE DATABASE IF NOT EXISTS ECommercePlatform;
USE ECommercePlatform;

-- Users Table: Stores information about registered users.
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Store hashed passwords (e.g., bcrypt)
    role ENUM('customer', 'admin') NOT NULL DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products Table: Contains details about the products available for sale.
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders Table: Stores information about orders placed by users.
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (total_amount >= 0),
    order_status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending',
    shipping_address TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- OrderDetails Table: A junction table linking Orders and Products, storing details for each item in an order.
CREATE TABLE OrderDetails (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0), -- Price of the product at the time of order
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE RESTRICT
);

-- Payments Table: Records payment transactions associated with orders.
CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method ENUM('credit_card', 'paypal', 'bank_transfer', 'stripe') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    transaction_id VARCHAR(255) UNIQUE, -- Unique ID from the payment gateway
    status ENUM('pending', 'completed', 'failed', 'refunded') NOT NULL DEFAULT 'pending',
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- Reviews Table: Stores customer reviews and ratings for products.
CREATE TABLE Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product_review (user_id, product_id) -- Ensures a user can review a specific product only once.
);
``` 

## 2. Sample Data Insertion 📊
Below is sample data for each table.
Note: ```password_hash``` values are placeholders. In a real application, use a strong hashing algorithm like bcrypt.
```sql
-- Users
INSERT INTO Users (username, email, password_hash, role) VALUES
('john_doe', 'john.doe@example.com', 'hashed_password1_placeholder', 'customer'),
('jane_smith', 'jane.smith@example.com', 'hashed_password2_placeholder', 'customer'),
('admin_user', 'admin@example.com', 'hashed_password_admin_placeholder', 'admin'),
('sam_wilson', 'sam.wilson@example.com', 'hashed_password3_placeholder', 'customer');

-- Products
INSERT INTO Products (product_name, category, price, stock_quantity, description) VALUES
('Laptop Pro 15', 'Electronics', 1200.00, 50, 'High-performance laptop for professionals.'),
('Wireless Mouse', 'Electronics', 25.00, 200, 'Ergonomic wireless mouse with long battery life.'),
('SQL for Beginners', 'Books', 30.00, 100, 'A comprehensive guide to SQL.'),
('Coffee Maker Deluxe', 'Home Appliances', 75.00, 75, 'Brews up to 12 cups of coffee.'),
('Smartphone X', 'Electronics', 800.00, 30, 'Latest generation smartphone with AI camera.');

-- Orders
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

-- Update total_amount in Orders table
-- This step is crucial. In a real system, this might be handled by application logic or triggers.
UPDATE Orders SET total_amount = (SELECT SUM(quantity * unit_price) FROM OrderDetails WHERE order_id = @order1_id) WHERE order_id = @order1_id;
UPDATE Orders SET total_amount = (SELECT SUM(quantity * unit_price) FROM OrderDetails WHERE order_id = @order2_id) WHERE order_id = @order2_id;
UPDATE Orders SET total_amount = (SELECT SUM(quantity * unit_price) FROM OrderDetails WHERE order_id = @order3_id) WHERE order_id = @order3_id;

-- Payments
INSERT INTO Payments (order_id, payment_method, amount, transaction_id, status) VALUES
(@order1_id, 'credit_card', (SELECT total_amount FROM Orders WHERE order_id = @order1_id), 'txn_abc123xyz', 'completed'),
(@order2_id, 'paypal', (SELECT total_amount FROM Orders WHERE order_id = @order2_id), 'txn_def456uvw', 'completed');

-- Reviews
INSERT INTO Reviews (product_id, user_id, rating, review_text) VALUES
(1, 1, 5, 'Absolutely love this laptop! Super fast and reliable.'),
(2, 1, 4, 'Good mouse, comfortable to use.'),
(3, 2, 5, 'Excellent book for learning SQL. Highly recommended.'),
(5, 2, 4, 'Great phone, camera is amazing but battery could be better.');
```

## 3. SQL Queries and Explanations ❓
This section provides SQL queries for various operations as required, along with explanations for each.

### Basic Retrieval Queries
- Retrieve the list of all products in a specific category (e.g., 'Electronics').
```sql
SELECT product_id, product_name, price, stock_quantity
FROM Products
WHERE category = 'Electronics';
```
Explanation: This query selects key product details (```product_id, product_name, price, stock_quantity```) from the Products table. It filters the results to include only those products where the category column matches 'Electronics'.

- Retrieve the details of a specific user by providing their user_id (e.g., ```user_id = 1```).
```sql
SELECT user_id, username, email, role, created_at
FROM Users
WHERE user_id = 1;
```
Explanation: This query fetches all relevant information (```user_id, username, email, role, created_at```) for a single user from the Users table. The WHERE clause filters the results to the user whose ```user_id``` is 1.

- Retrieve the order history for a particular user (e.g., ```user_id = 1```).
```sql
SELECT order_id, order_date, total_amount, order_status
FROM Orders
WHERE user_id = 1
ORDER BY order_date DESC;
```
Explanation: This query lists all orders (```order_id, order_date, total_amount, order_status```) placed by a specific user, identified by user_id = 1. The results are sorted by order_date in descending order, showing the most recent orders first.

- Retrieve the products in an order along with their quantities and prices (e.g., for ```order_id = 1```).
```sql
SELECT p.product_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS item_total
FROM OrderDetails od
JOIN Products p ON od.product_id = p.product_id
WHERE od.order_id = 1; -- Use an actual order_id from your sample data
```
Explanation: This query joins the OrderDetails table (aliased as od) with the Products table (aliased as p) using the ```product_id```. It retrieves the product name, quantity ordered, unit price at the time of order, and calculates the total for each item line (```item_total```). The ```WHERE``` clause filters these details for a specific order_id.

- Retrieve the average rating of a product (e.g., ```product_id = 1```).
```sql
SELECT AVG(rating) AS average_rating
FROM Reviews
WHERE product_id = 1;
```
Explanation: This query calculates the average rating for a specific product (```where product_id = 1```) by using the ```AVG()``` aggregate function on the rating column in the Reviews table.

- Retrieve the total revenue for a given month (e.g., current month of the current year).
```sql
SELECT SUM(p.amount) AS total_revenue
FROM Payments p
-- JOIN Orders o ON p.order_id = o.order_id -- Optional join if order details needed for filtering
WHERE p.status = 'completed'
  AND MONTH(p.payment_date) = MONTH(CURDATE()) -- For current month
  AND YEAR(p.payment_date) = YEAR(CURDATE());  -- For current year
```
Explanation: This query calculates the total revenue by summing the amount from the Payments table for all payments that have a status of 'completed'. It filters these payments to include only those made in the current month and current year using ```MONTH(CURDATE())``` and ```YEAR(CURDATE())```. The join to Orders is commented out as it's optional if only payment data is directly used for revenue calculation based on payment status and date.

### Data Modification Queries
- Add a new product to the inventory.
```sql
INSERT INTO Products (product_name, category, price, stock_quantity, description)
VALUES ('Gaming Keyboard RGB', 'Electronics', 89.99, 150, 'Mechanical gaming keyboard with customizable RGB lighting.');
```
Explanation: This query adds a new record to the Products table with the specified details for ```product_name, category, price, stock_quantity```, and ```description```.

- Place a new order for a user (e.g., ```user_id = 2```).
This is a multi-step process, ideally handled within a transaction in an application.
```sql
-- Step 1: Create the order
INSERT INTO Orders (user_id, order_status, shipping_address, total_amount)
VALUES (2, 'pending', '101 New Street, Townsville, USA', 0.00); -- Initial total, will be updated
SET @new_order_id = LAST_INSERT_ID();

-- Step 2: Add order details (e.g., user orders 1 Wireless Mouse (ID 2) and 1 Coffee Maker (ID 4))
INSERT INTO OrderDetails (order_id, product_id, quantity, unit_price)
VALUES (@new_order_id, 2, 1, (SELECT price FROM Products WHERE product_id = 2)),
       (@new_order_id, 4, 1, (SELECT price FROM Products WHERE product_id = 4));

-- Step 3: Update total_amount for the order
UPDATE Orders o
SET total_amount = (
    SELECT SUM(od.quantity * od.unit_price)
    FROM OrderDetails od
    WHERE od.order_id = o.order_id
)
WHERE o.order_id = @new_order_id;

-- Step 4: Update stock quantity (ideally a trigger)
UPDATE Products SET stock_quantity = stock_quantity - 1 WHERE product_id = 2;
UPDATE Products SET stock_quantity = stock_quantity - 1 WHERE product
```
