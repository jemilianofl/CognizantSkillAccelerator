-- Basic retreival Queries
-- Retrieve the list of all products in a specific category (e.g., 'Electronics').
SELECT product_id, product_name, price, stock_quantity
FROM Products
WHERE category = 'Electronics';

-- Retrieve the details of a specific user by providing their user_id (e.g., user_id = 1).
SELECT user_id, username, email, role, created_at
FROM Users
WHERE user_id = 1;

-- Retrieve the order history for a particular user (e.g., user_id = 1).
SELECT order_id, order_date, total_amount, order_status
FROM Orders
WHERE user_id = 1
ORDER BY order_date DESC;

-- Retrieve the products in an order along with their quantities and prices (e.g., order_id = @order1_id)
SELECT p.product_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS item_total
FROM OrderDetails od
JOIN Products p ON od.product_id = p.product_id
WHERE od.order_id = @order1_id; -- Use actual ID or variable

-- Retrieve the average rating of a product (e.g., product_id = 1).
SELECT AVG(rating) AS average_rating
FROM Reviews
WHERE product_id = 1;

-- Retrieve the total revenue for a given month (e.g., May 2025).
SELECT SUM(p.amount) AS total_revenue
FROM Payments p
JOIN Orders o ON p.order_id = o.order_id
WHERE p.status = 'completed'
  AND MONTH(p.payment_date) = 5
  AND YEAR(p.payment_date) = 2025; -- Adjust month/year as needed