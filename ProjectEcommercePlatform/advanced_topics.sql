-- Stored Procedure: Generate a Report on Most Active Users
DELIMITER //

CREATE PROCEDURE GetMostActiveUsers(IN limit_count INT)
BEGIN
    SELECT
        u.user_id,
        u.username,
        u.email,
        COUNT(o.order_id) AS total_orders_placed
    FROM Users u
    JOIN Orders o ON u.user_id = o.user_id
    GROUP BY u.user_id, u.username, u.email
    ORDER BY total_orders_placed DESC
    LIMIT limit_count;
END //

DELIMITER ;

-- How to call it:
-- CALL GetMostActiveUsers(5); -- Get top 5 active users

-- Trigger: Update Stock Quantity and Order Total Amount
DELIMITER //

CREATE TRIGGER AfterOrderDetailInsert_UpdateStock
AFTER INSERT ON OrderDetails
FOR EACH ROW
BEGIN
    UPDATE Products
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END //

DELIMITER ;

-- Trigger to update Orders.total_amount AFTER an OrderDetail is inserted or updated.
DELIMITER //

CREATE TRIGGER AfterOrderDetailInsert_UpdateOrderTotal
AFTER INSERT ON OrderDetails
FOR EACH ROW
BEGIN
    UPDATE Orders
    SET total_amount = (
        SELECT SUM(od.quantity * od.unit_price)
        FROM OrderDetails od
        WHERE od.order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END //

CREATE TRIGGER AfterOrderDetailUpdate_UpdateOrderTotal
AFTER UPDATE ON OrderDetails
FOR EACH ROW
BEGIN
    UPDATE Orders
    SET total_amount = (
        SELECT SUM(od.quantity * od.unit_price)
        FROM OrderDetails od
        WHERE od.order_id = NEW.order_id -- or OLD.order_id if order_id cannot change
    )
    WHERE order_id = NEW.order_id;
END //

CREATE TRIGGER AfterOrderDetailDelete_UpdateOrderTotal
AFTER DELETE ON OrderDetails
FOR EACH ROW
BEGIN
    UPDATE Orders
    SET total_amount = (
        SELECT SUM(od.quantity * od.unit_price)
        FROM OrderDetails od
        WHERE od.order_id = OLD.order_id
    )
    WHERE order_id = OLD.order_id;
END //

DELIMITER ;

-- Trigger: Automatically Update Order Status
DELIMITER //

CREATE TRIGGER AfterPaymentCompletion_UpdateOrderStatus
AFTER UPDATE ON Payments
FOR EACH ROW
BEGIN
    IF NEW.status = 'completed' AND OLD.status != 'completed' THEN
        UPDATE Orders
        SET order_status = 'processing' -- Or next appropriate status
        WHERE order_id = NEW.order_id AND order_status = 'pending';
    END IF;
END //

DELIMITER ;