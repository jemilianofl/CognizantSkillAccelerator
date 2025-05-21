-- 3. SQL Queries ❓
-- ---
-- Basic Retrieval Queries

-- Retrieve the posts and activities of a user's timeline.
-- (For user with user_id = 1, showing their posts and posts from users they follow)
SELECT p.post_id, p.post_text, p.media_url, p.created_at, u.username AS author_username, u.profile_picture_url AS author_profile_pic, p.like_count, p.comment_count
FROM Posts p
JOIN Users u ON p.user_id = u.user_id
WHERE p.user_id = 1 -- User's own posts
   OR p.user_id IN (SELECT following_id FROM Follows WHERE follower_id = 1) -- Posts from users they follow
ORDER BY p.created_at DESC
LIMIT 20;

-- Retrieve the comments and likes for a specific post.
-- (For post with post_id = @anna_post1_id)
-- Comments for the post
SELECT c.comment_id, c.comment_text, c.created_at, u.username AS commenter_username, u.profile_picture_url AS commenter_profile_pic
FROM Comments c
JOIN Users u ON c.user_id = u.user_id
WHERE c.post_id = @anna_post1_id
ORDER BY c.created_at ASC;

-- Likes for the post (who liked it)
SELECT l.like_id, l.created_at, u.username AS liker_username, u.profile_picture_url AS liker_profile_pic
FROM Likes l
JOIN Users u ON l.user_id = u.user_id
WHERE l.post_id = @anna_post1_id
ORDER BY l.created_at DESC;

-- Retrieve the list of followers for a user.
-- (For user with user_id = 1 - Anna)
SELECT u.user_id, u.username, u.profile_picture_url, f.created_at AS follow_date
FROM Users u
JOIN Follows f ON u.user_id = f.follower_id
WHERE f.following_id = 1
ORDER BY f.created_at DESC;

-- Retrieve unread messages for a user.
-- (For user with user_id = 1 - Anna)
SELECT m.message_id, m.message_text, m.created_at, u_sender.username AS sender_username, u_sender.profile_picture_url AS sender_profile_pic
FROM Messages m
JOIN Users u_sender ON m.sender_id = u_sender.user_id
WHERE m.receiver_id = 1 AND m.is_read = FALSE
ORDER BY m.created_at DESC;

-- Retrieve the most liked posts.
SELECT p.post_id, p.post_text, p.media_url, p.created_at, u.username AS author_username, p.like_count, p.comment_count
FROM Posts p
JOIN Users u ON p.user_id = u.user_id
ORDER BY p.like_count DESC
LIMIT 10;

-- Retrieve the latest notifications for a user.
-- (For user with user_id = 1 - Anna)
SELECT n.notification_id, n.notification_text, n.notification_type, n.target_url, n.is_read, n.created_at, u_actor.username AS actor_username
FROM Notifications n
LEFT JOIN Users u_actor ON n.actor_id = u_actor.user_id
WHERE n.user_id = 1
ORDER BY n.created_at DESC
LIMIT 15;

-- Data Modification Queries

-- Add a new post to the platform.
-- (User user_id = 2 - Brian - adds a new post)
INSERT INTO Posts (user_id, post_text, media_url)
VALUES (2, 'Just deployed a new feature! Very excited for users to try it out. #softwaredevelopment', 'https://example.com/new_feature.gif');
-- (A trigger should ideally update Users.post_count for user_id = 2)

-- Comment on a post.
-- (User user_id = 1 - Anna - comments on Brian's first post, post_id = @brian_post1_id)
INSERT INTO Comments (post_id, user_id, comment_text)
VALUES (@brian_post1_id, 1, 'This is a fantastic update, Brian! Can''t wait to see more.');
-- (A trigger should ideally update Posts.comment_count for post_id = @brian_post1_id and create a notification)

-- Update user profile information.
-- (User user_id = 1 - Anna - updates her bio)
UPDATE Users
SET bio = 'Traveling the globe and sharing my adventures. Still coding in my spare time.', profile_picture_url = 'https://example.com/anna_new_profile.jpg'
WHERE user_id = 1;

-- Remove a like from a post.
-- (User user_id = 2 - Brian - removes his like from Anna's first post, post_id = @anna_post1_id)
DELETE FROM Likes
WHERE post_id = @anna_post1_id AND user_id = 2;
-- (A trigger should ideally update Posts.like_count for post_id = @anna_post1_id)

-- Complex Queries

-- Identify users with the most followers.
SELECT user_id, username, profile_picture_url, follower_count
FROM Users
ORDER BY follower_count DESC
LIMIT 10;

-- Find the most active users based on post count and interaction (sum of likes and comments on their posts).
SELECT
    u.user_id,
    u.username,
    u.profile_picture_url,
    u.post_count,
    COALESCE(SUM(p.like_count), 0) AS total_likes_received,
    COALESCE(SUM(p.comment_count), 0) AS total_comments_received,
    (u.post_count * 5) + COALESCE(SUM(p.like_count), 0) + (COALESCE(SUM(p.comment_count), 0) * 2) AS activity_score
FROM Users u
LEFT JOIN Posts p ON u.user_id = p.user_id
GROUP BY u.user_id, u.username, u.profile_picture_url, u.post_count
ORDER BY activity_score DESC
LIMIT 10;

-- Calculate the average number of comments per post.
SELECT AVG(comment_count) AS average_comments_per_post
FROM Posts;
