-- Add a new product to the inventory.
INSERT INTO Products (product_name, category, price, stock_quantity, description)
VALUES ('Gaming Keyboard RGB', 'Electronics', 89.99, 150, 'Mechanical gaming keyboard with customizable RGB lighting.');

-- Place a new order for a user (e.g., user_id = 2). This is a multi-step process, often handled by application logic and transactions.
-- Step 1: Create the order
INSERT INTO Orders (user_id, order_status, shipping_address, total_amount)
VALUES (2, 'pending', '101 New Street, Townsville, USA', 0.00); -- Initial total_amount, update later
SET @new_order_id = LAST_INSERT_ID();

-- Step 2: Add order details (e.g., user orders 1 Wireless Mouse and 1 Coffee Maker)
-- Assume product_id for Wireless Mouse is 2, Coffee Maker is 4
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

-- Step 4: (Simulate) Update stock quantity (ideally a trigger or careful transaction)
UPDATE Products SET stock_quantity = stock_quantity - 1 WHERE product_id = 2;
UPDATE Products SET stock_quantity = stock_quantity - 1 WHERE product_id = 4;

-- Step 5: Record payment (example)
INSERT INTO Payments (order_id, payment_method, amount, transaction_id, status)
VALUES (@new_order_id, 'stripe', (SELECT total_amount FROM Orders WHERE order_id = @new_order_id), 'txn_new789', 'pending');

SELECT * FROM Orders WHERE order_id = @new_order_id;
SELECT * FROM OrderDetails WHERE order_id = @new_order_id;

-- Update the stock quantity of a product (e.g., product_id = 1, new stock = 45).
UPDATE Products
SET stock_quantity = 45
WHERE product_id = 1;

-- Remove a user's review (e.g., review_id = 1).
DELETE FROM Reviews
WHERE review_id = 1;
-- Or, if you know user_id and product_id:
-- DELETE FROM Reviews WHERE user_id = 1 AND product_id = 1;
