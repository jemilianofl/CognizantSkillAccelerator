-- Identify the top N selling products (e.g., top 3).
SELECT
    p.product_id,
    p.product_name,
    SUM(od.quantity) AS total_quantity_sold
FROM OrderDetails od
JOIN Products p ON od.product_id = p.product_id
JOIN Orders o ON od.order_id = o.order_id
WHERE o.order_status IN ('shipped', 'delivered') -- Consider only completed/shipped orders
GROUP BY p.product_id, p.product_name
ORDER BY total_quantity_sold DESC
LIMIT 3;

-- Find users who have placed orders exceeding a certain amount (e.g., total_amount > 1000).
SELECT
    u.user_id,
    u.username,
    u.email,
    SUM(o.total_amount) AS total_spent_by_user
FROM Users u
JOIN Orders o ON u.user_id = o.user_id
WHERE o.order_status IN ('shipped', 'delivered', 'processing') -- Consider valid orders
GROUP BY u.user_id, u.username, u.email
HAVING SUM(o.total_amount) > 100.00; -- Adjusted for sample data, use 1000 for real data

-- Calculate the overall average rating for each product category.
SELECT
    p.category,
    AVG(r.rating) AS average_category_rating,
    COUNT(DISTINCT p.product_id) AS number_of_products_in_category,
    COUNT(r.review_id) AS number_of_reviews_in_category
FROM Products p
LEFT JOIN Reviews r ON p.product_id = r.product_id
WHERE p.category IS NOT NULL
GROUP BY p.category
ORDER BY average_category_rating DESC;
