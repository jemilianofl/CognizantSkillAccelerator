# Social Media Platform Database Project 📱

This document provides comprehensive documentation for a sophisticated relational database designed for a social media platform using MySQL. It includes the database schema, sample data, SQL queries with explanations, and examples of advanced database objects like triggers and stored procedures.

---

## Table of Contents
- [Social Media Platform Database Project 📱](#social-media-platform-database-project-)
  - [Table of Contents](#table-of-contents)
  - [1. Database Schema 🧱](#1-database-schema-)
  - [2. Sample Data Insertion 📊](#2-sample-data-insertion-)
  - [3. SQL Queries and Explanations ❓](#3-sql-queries-and-explanations-)
    - [Data Modification Queries](#data-modification-queries)
    - [Complex Queries](#complex-queries)
  - [4. Advanced Topics (Examples) ⚙️](#4-advanced-topics-examples-️)
    - [Triggers](#triggers)
    - [Stored Procedures](#stored-procedures)

---

## 1. Database Schema 🧱

The database is named `SocialMediaPlatform`. The schema is designed to handle user profiles, posts, comments, likes, follows, messages, and notifications.

```sql
CREATE DATABASE IF NOT EXISTS SocialMediaPlatform;
USE SocialMediaPlatform;

-- Users Table: Stores information about registered users, including profile details and aggregated counts.
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Store hashed passwords (e.g., bcrypt)
    date_of_birth DATE,
    profile_picture_url VARCHAR(255),
    bio TEXT,
    post_count INT DEFAULT 0,          -- Maintained by triggers
    follower_count INT DEFAULT 0,      -- Maintained by triggers
    following_count INT DEFAULT 0,     -- Maintained by triggers
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Posts Table: Contains details about the posts created by users.
CREATE TABLE Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    post_text TEXT,
    media_url VARCHAR(255), -- URL for image or video content
    like_count INT DEFAULT 0,           -- Maintained by triggers
    comment_count INT DEFAULT 0,        -- Maintained by triggers
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE -- If user is deleted, their posts are also deleted.
);

-- Comments Table: Stores comments made by users on posts.
CREATE TABLE Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE CASCADE, -- If post is deleted, comments are deleted.
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE  -- If user is deleted, their comments are deleted.
);

-- Likes Table: Records likes given by users to posts.
CREATE TABLE Likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_post_like (user_id, post_id) -- Ensures a user can like a post only once.
);

-- Follows Table: Manages the follower-following relationships between users.
CREATE TABLE Follows (
    follow_id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT NOT NULL, -- The user who is following
    following_id INT NOT NULL, -- The user who is being followed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_follow_pair (follower_id, following_id) -- Prevents duplicate follow entries.
);

-- Messages Table: Stores private messages exchanged between users.
CREATE TABLE Messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Notifications Table: Stores notifications for users about various activities.
CREATE TABLE Notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL, -- The user who receives the notification
    actor_id INT,         -- The user who performed the action (can be NULL for system notifications).
    post_id INT,          -- Optional, if the notification is related to a post.
    message_id INT,       -- Optional, if the notification is related to a message.
    notification_type ENUM('like', 'comment', 'follow', 'message', 'mention', 'system') NOT NULL,
    notification_text VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    target_url VARCHAR(255), -- URL to navigate to when notification is clicked (e.g., /posts/123)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES Users(user_id) ON DELETE SET NULL, -- If actor is deleted, notification remains but actor_id is null.
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE SET NULL,   -- If post is deleted, notification remains but post_id is null.
    FOREIGN KEY (message_id) REFERENCES Messages(message_id) ON DELETE SET NULL -- If message is deleted, notification remains.
);
```
---

## 2. Sample Data Insertion 📊
Below is sample data for each table.
Note: ```password_hash``` values are placeholders. In a real application, use a strong hashing algorithm like bcrypt.

```sql
-- Users
INSERT INTO Users (username, email, password_hash, date_of_birth, profile_picture_url, bio) VALUES
('anna_dev', 'anna@example.com', 'hash_anna', '1998-05-20', '[https://example.com/anna.jpg](https://example.com/anna.jpg)', 'Software developer and AI enthusiast.'),
('brian_traveler', 'brian@example.com', 'hash_brian', '1992-11-10', '[https://example.com/brian.jpg](https://example.com/brian.jpg)', 'Exploring the world and sharing my adventures.'),
('clara_chef', 'clara@example.com', 'hash_clara', '1995-02-15', NULL, 'Food lover and recipe creator.'),
('dave_gamer', 'dave@example.com', 'hash_dave', '2000-08-01', '[https://example.com/dave.jpg](https://example.com/dave.jpg)', 'Professional gamer and streamer.');

-- Posts (assuming user_ids are 1, 2, 3, 4 for Anna, Brian, Clara, Dave respectively)
INSERT INTO Posts (user_id, post_text, media_url) VALUES
(1, 'New blog post about Python! #programming #python', NULL),
(2, 'Incredible sunset at the beach. #travel #nature', '[https://example.com/beach.jpg](https://example.com/beach.jpg)'),
(1, 'Working on an exciting project. More details soon! #webdevelopment', NULL),
(3, 'Recipe of the day: Creamy mushroom pasta. 🍝 #cooking #recipes', '[https://example.com/pasta.jpg](https://example.com/pasta.jpg)');
SET @anna_post1_id = 1; SET @brian_post1_id = 2; SET @anna_post2_id = 3; SET @clara_post1_id = 4;

-- Comments
INSERT INTO Comments (post_id, user_id, comment_text) VALUES
(@anna_post1_id, 2, 'Excellent article, Anna! Very useful.'),
(@anna_post1_id, 3, 'Loved it, I''m going to check it out.'),
(@brian_post1_id, 1, 'What a spectacular photo, Brian!'),
(@clara_post1_id, 4, 'Looks delicious, Clara. Thanks for the recipe!');

-- Likes
INSERT INTO Likes (post_id, user_id) VALUES
(@anna_post1_id, 2), (@anna_post1_id, 3), (@anna_post1_id, 4),
(@brian_post1_id, 1), (@brian_post1_id, 3),
(@clara_post1_id, 1), (@clara_post1_id, 2), (@clara_post1_id, 4);

-- Follows
INSERT INTO Follows (follower_id, following_id) VALUES
(1, 2), (1, 3), -- Anna follows Brian and Clara
(2, 1), -- Brian follows Anna
(3, 1), (3, 2), (3, 4), -- Clara follows Anna, Brian, and Dave
(4, 1); -- Dave follows Anna

-- Messages
INSERT INTO Messages (sender_id, receiver_id, message_text) VALUES
(1, 2, 'Hey Brian! How are you? Saw your beach photos, incredible!'),
(2, 1, 'Hi Anna! All good, thanks. Glad you liked them.'),
(3, 4, 'Dave, how about we organize a game soon?');
SET @msg_id_anna_to_brian = 1; SET @msg_id_brian_to_anna = 2; SET @msg_id_clara_to_dave = 3;

-- Notifications (These would typically be generated by triggers or application logic, but here for completeness)
INSERT INTO Notifications (user_id, actor_id, post_id, notification_type, notification_text, target_url) VALUES
(1, 2, @anna_post1_id, 'like', 'Brian Traveler liked your post.', CONCAT('/posts/', @anna_post1_id)),
(2, 1, @brian_post1_id, 'comment', 'Anna Dev commented on your post.', CONCAT('/posts/', @brian_post1_id)),
(2, 1, NULL, 'follow', 'Anna Dev started following you.', '/profile/1');
INSERT INTO Notifications (user_id, actor_id, message_id, notification_type, notification_text, target_url) VALUES
(1, 2, @msg_id_brian_to_anna, 'message', 'You have a new message from Brian Traveler.', CONCAT('/messages/thread/', 2)); -- Assuming thread URL uses other user's ID

-- Manually update counts for sample data (These should ideally be handled by triggers, see Advanced Topics)
UPDATE Users u SET post_count = (SELECT COUNT(*) FROM Posts p WHERE p.user_id = u.user_id);
UPDATE Users u SET follower_count = (SELECT COUNT(*) FROM Follows f WHERE f.following_id = u.user_id);
UPDATE Users u SET following_count = (SELECT COUNT(*) FROM Follows f WHERE f.follower_id = u.user_id);
UPDATE Posts p SET like_count = (SELECT COUNT(*) FROM Likes l WHERE l.post_id = p.post_id);
UPDATE Posts p SET comment_count = (SELECT COUNT(*) FROM Comments c WHERE c.post_id = p.post_id);
```


## 3. SQL Queries and Explanations ❓
This section provides SQL queries for various operations, along with explanations for each.

Basic Retrieval Queries
- Retrieve the posts and activities of a user's timeline. (For user with ```user_id = 1```, showing their posts and posts from users they follow)

```sql
SELECT
    p.post_id, p.post_text, p.media_url, p.created_at,
    u.username AS author_username, u.profile_picture_url AS author_profile_pic,
    p.like_count, p.comment_count
FROM Posts p
JOIN Users u ON p.user_id = u.user_id
WHERE p.user_id = 1 -- User's own posts
   OR p.user_id IN (SELECT following_id FROM Follows WHERE follower_id = 1) -- Posts from users they follow
ORDER BY p.created_at DESC
LIMIT 20;
```

- Retrieve the comments and likes for a specific post. (For post with ```post_id = @anna_post1_id``` which is 1, based on sample data)
```sql
-- Comments for the post
SELECT
    c.comment_id, c.comment_text, c.created_at,
    u.username AS commenter_username, u.profile_picture_url AS commenter_profile_pic
FROM Comments c
JOIN Users u ON c.user_id = u.user_id
WHERE c.post_id = 1 -- Replace with actual post_id variable or value
ORDER BY c.created_at ASC;

-- Users who liked the post
SELECT
    l.like_id, l.created_at,
    u.username AS liker_username, u.profile_picture_url AS liker_profile_pic
FROM Likes l
JOIN Users u ON l.user_id = u.user_id
WHERE l.post_id = 1 -- Replace with actual post_id variable or value
ORDER BY l.created_at DESC;
```

- Retrieve the list of followers for a user. (For user with ```user_id = 1``` - Anna)
```sql
SELECT
    u.user_id, u.username, u.profile_picture_url,
    f.created_at AS follow_date
FROM Users u
JOIN Follows f ON u.user_id = f.follower_id
WHERE f.following_id = 1 -- User ID of the person whose followers are being fetched
ORDER BY f.created_at DESC;
```

- Retrieve unread messages for a user. (For user with ```user_id = 1``` - Anna)
```sql
SQL

SELECT
    m.message_id, m.message_text, m.created_at,
    u_sender.username AS sender_username, u_sender.profile_picture_url AS sender_profile_pic
FROM Messages m
JOIN Users u_sender ON m.sender_id = u_sender.user_id
WHERE m.receiver_id = 1 AND m.is_read = FALSE
ORDER BY m.created_at DESC;
```

- Retrieve the most liked posts.

```sql

SELECT
    p.post_id, p.post_text, p.media_url, p.created_at,
    u.username AS author_username,
    p.like_count, p.comment_count
FROM Posts p
JOIN Users u ON p.user_id = u.user_id
ORDER BY p.like_count DESC
LIMIT 10;
```

- Retrieve the latest notifications for a user. (For user with user_id = 1 - Anna)

```sql
SELECT
    n.notification_id, n.notification_text, n.notification_type,
    n.target_url, n.is_read, n.created_at,
    u_actor.username AS actor_username
FROM Notifications n
LEFT JOIN Users u_actor ON n.actor_id = u_actor.user_id -- LEFT JOIN as actor can be NULL
WHERE n.user_id = 1
ORDER BY n.created_at DESC
LIMIT 15;
```

### Data Modification Queries
- Add a new post to the platform. (User ```user_id = 2``` - Brian - adds a new post)
  
```sql
INSERT INTO Posts (user_id, post_text, media_url)
VALUES (2, 'Just deployed a new feature! Very excited for users to try it out. #softwaredevelopment', '[https://example.com/new_feature.gif](https://example.com/new_feature.gif)');
```

- Comment on a post. (User user_id = 1 - Anna - comments on Brian's first post, post_id = @brian_post1_id, which is 2)

```sql
INSERT INTO Comments (post_id, user_id, comment_text)
VALUES (2, 1, 'This is a fantastic update, Brian! Can''t wait to see more.');
```

- Update user profile information.
(User user_id = 1 - Anna - updates her bio and profile picture)

```sql
UPDATE Users
SET bio = 'Traveling the globe and sharing my adventures. Still coding in my spare time.',
    profile_picture_url = '[https://example.com/anna_new_profile.jpg](https://example.com/anna_new_profile.jpg)'
WHERE user_id = 1;
```

- Remove a like from a post. (User user_id = 2 - Brian - removes his like from Anna's first post, post_id = @anna_post1_id, which is 1)

```sql
DELETE FROM Likes
WHERE post_id = 1 AND user_id = 2;
```

### Complex Queries
- Identify users with the most followers.

```sql
SELECT user_id, username, profile_picture_url, follower_count
FROM Users
ORDER BY follower_count DESC
LIMIT 10;
```

- Find the most active users based on post count and interaction (likes + comments on their posts).
```sql
SELECT
    u.user_id,
    u.username,
    u.profile_picture_url,
    u.post_count,
    COALESCE(SUM(p.like_count), 0) AS total_likes_received_on_posts,
    COALESCE(SUM(p.comment_count), 0) AS total_comments_received_on_posts,
    -- Activity Score: posts * 5 points, likes received * 1 point, comments received * 2 points
    (u.post_count * 5) + COALESCE(SUM(p.like_count), 0) + (COALESCE(SUM(p.comment_count), 0) * 2) AS activity_score
FROM Users u
LEFT JOIN Posts p ON u.user_id = p.user_id
GROUP BY u.user_id, u.username, u.profile_picture_url, u.post_count
ORDER BY activity_score DESC
LIMIT 10;
```

- Calculate the average number of comments per post.
```sql
SELECT AVG(comment_count) AS average_comments_per_post
FROM Posts;
```

## 4. Advanced Topics (Examples) ⚙️
This section provides examples of triggers and stored procedures to automate tasks and encapsulate complex logic.

### Triggers
Triggers are used to automatically maintain denormalized counts and create notifications.

- Update Posts.like_count and notify on new like.

```sql
DELIMITER //
CREATE TRIGGER AfterLikeInsert_UpdatePostLikeCountAndNotify
AFTER INSERT ON Likes
FOR EACH ROW
BEGIN
    DECLARE post_author_id INT;
    DECLARE liker_username VARCHAR(50);

    -- Update like count on the post
    UPDATE Posts SET like_count = like_count + 1 WHERE post_id = NEW.post_id;

    -- Get the post author and the username of the user who liked
    SELECT user_id INTO post_author_id FROM Posts WHERE post_id = NEW.post_id;
    SELECT username INTO liker_username FROM Users WHERE user_id = NEW.user_id;

    -- Create notification for the post author (if not the one who liked their own post)
    IF post_author_id != NEW.user_id THEN
        INSERT INTO Notifications (user_id, actor_id, post_id, notification_type, notification_text, target_url)
        VALUES (post_author_id, NEW.user_id, NEW.post_id, 'like',
                CONCAT(liker_username, ' liked your post.'),
                CONCAT('/posts/', NEW.post_id));
    END IF;
END //
DELIMITER ;
```

- Update Posts.like_count on like removal.

```sql
DELIMITER //
CREATE TRIGGER AfterLikeDelete_UpdatePostLikeCount
AFTER DELETE ON Likes
FOR EACH ROW
BEGIN
    UPDATE Posts SET like_count = GREATEST(0, like_count - 1) WHERE post_id = OLD.post_id;
END //
DELIMITER ;
```

- Update Users.follower_count and Users.following_count and notify on new follow.

```sql
DELIMITER //
CREATE TRIGGER AfterFollowInsert_UpdateUserCountsAndNotify
AFTER INSERT ON Follows
FOR EACH ROW
BEGIN
    DECLARE follower_username_val VARCHAR(50);

    -- Update follow counts
    UPDATE Users SET following_count = following_count + 1 WHERE user_id = NEW.follower_id;
    UPDATE Users SET follower_count = follower_count + 1 WHERE user_id = NEW.following_id;

    -- Get the username of the follower
    SELECT username INTO follower_username_val FROM Users WHERE user_id = NEW.follower_id;

    -- Create notification for the user being followed
    INSERT INTO Notifications (user_id, actor_id, notification_type, notification_text, target_url)
    VALUES (NEW.following_id, NEW.follower_id, 'follow',
            CONCAT(follower_username_val, ' started following you.'),
            CONCAT('/profile/', NEW.follower_id));
END //
DELIMITER ;
```

- Update user counts on unfollow.

```sql
DELIMITER //
CREATE TRIGGER AfterFollowDelete_UpdateUserCounts
AFTER DELETE ON Follows
FOR EACH ROW
BEGIN
    UPDATE Users SET following_count = GREATEST(0, following_count - 1) WHERE user_id = OLD.follower_id;
    UPDATE Users SET follower_count = GREATEST(0, follower_count - 1) WHERE user_id = OLD.following_id;
END //
DELIMITER ;
```

- Update Users.post_count on post creation/deletion.

```sql
DELIMITER //
CREATE TRIGGER AfterPostInsert_UpdateUserPostCount
AFTER INSERT ON Posts
FOR EACH ROW
BEGIN
    UPDATE Users SET post_count = post_count + 1 WHERE user_id = NEW.user_id;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER AfterPostDelete_UpdateUserPostCount
AFTER DELETE ON Posts
FOR EACH ROW
BEGIN
    UPDATE Users SET post_count = GREATEST(0, post_count - 1) WHERE user_id = OLD.user_id;
END //
DELIMITER ;
```

- Update Posts.comment_count and notify on new comment.

```sql
DELIMITER //
CREATE TRIGGER AfterCommentInsert_UpdatePostCommentCountAndNotify
AFTER INSERT ON Comments
FOR EACH ROW
BEGIN
    DECLARE post_author_id_val INT;
    DECLARE commenter_username_val VARCHAR(50);

    UPDATE Posts SET comment_count = comment_count + 1 WHERE post_id = NEW.post_id;

    SELECT user_id INTO post_author_id_val FROM Posts WHERE post_id = NEW.post_id;
    SELECT username INTO commenter_username_val FROM Users WHERE user_id = NEW.user_id;

    IF post_author_id_val != NEW.user_id THEN -- Do not notify if user comments on their own post
        INSERT INTO Notifications (user_id, actor_id, post_id, notification_type, notification_text, target_url)
        VALUES (post_author_id_val, NEW.user_id, NEW.post_id, 'comment',
                CONCAT(commenter_username_val, ' commented on your post.'),
                CONCAT('/posts/', NEW.post_id, '#comment-', NEW.comment_id));
    END IF;
END //
DELIMITER ;
```

- Update Posts.comment_count on comment deletion.

```
DELIMITER //
CREATE TRIGGER AfterCommentDelete_UpdatePostCommentCount
AFTER DELETE ON Comments
FOR EACH ROW
BEGIN
    UPDATE Posts SET comment_count = GREATEST(0, comment_count - 1) WHERE post_id = OLD.post_id;
END //
DELIMITER ;
```
- Automatically notify users of new messages.

```sql
DELIMITER //
CREATE TRIGGER AfterMessageInsert_NotifyReceiver
AFTER INSERT ON Messages
FOR EACH ROW
BEGIN
    DECLARE sender_username_val VARCHAR(50);
    SELECT username INTO sender_username_val FROM Users WHERE user_id = NEW.sender_id;

    INSERT INTO Notifications (user_id, actor_id, message_id, notification_type, notification_text, target_url)
    VALUES (NEW.receiver_id, NEW.sender_id, NEW.message_id, 'message',
            CONCAT('You have a new message from ', sender_username_val, '.'),
            CONCAT('/messages/thread/', NEW.sender_id)); -- Example target URL to the message thread with the sender
END //
DELIMITER ;
```

### Stored Procedures
- Generate personalized follow recommendations (simplified example).
This procedure suggests users to follow based on "friends of friends" logic: users followed by people whom the current user already follows, but doesn't yet follow themselves.

```sql
DELIMITER //
CREATE PROCEDURE GetFollowRecommendations(IN current_user_id_param INT, IN limit_count_param INT)
BEGIN
    SELECT DISTINCT
        u.user_id,
        u.username,
        u.profile_picture_url,
        u.bio,
        -- Calculate how many of the current user's friends also follow this recommended user
        (SELECT COUNT(DISTINCT f_mutual.follower_id)
         FROM Follows f_mutual
         WHERE f_mutual.following_id = u.user_id -- The recommended user is being followed
           AND f_mutual.follower_id IN (SELECT following_id FROM Follows WHERE follower_id = current_user_id_param) -- by a friend of the current user
        ) AS mutual_friends_following_them_count
    FROM Users u
    -- Find users (u) who are followed by someone (f1.follower_id)
    JOIN Follows f1 ON u.user_id = f1.following_id
    -- Ensure that this "someone" (f1.follower_id) is also followed by the current_user_id (making them a "friend")
    JOIN Follows f2 ON f1.follower_id = f2.following_id AND f2.follower_id = current_user_id_param
    WHERE u.user_id != current_user_id_param -- Don't recommend oneself
      AND u.user_id NOT IN (SELECT following_id FROM Follows WHERE follower_id = current_user_id_param) -- Don't recommend users already followed
    GROUP BY u.user_id, u.username, u.profile_picture_url, u.bio -- Group to make DISTINCT work with aggregate-like calculation in SELECT
    ORDER BY mutual_friends_following_them_count DESC, RAND() -- Prioritize by count of mutual connections, then randomize
    LIMIT limit_count_param;
END //
DELIMITER ;
```

- Procedure to follow/unfollow a user.
(Note: With the Follows triggers already defined for count updates and notifications, this procedure primarily serves to encapsulate the follow/unfollow action.)

```sql
DELIMITER //
CREATE PROCEDURE ToggleFollowUser(IN p_follower_id INT, IN p_following_id INT)
BEGIN
    DECLARE is_already_following BOOLEAN;
    SELECT EXISTS(SELECT 1 FROM Follows WHERE follower_id = p_follower_id AND following_id = p_following_id) INTO is_already_following;

    IF p_follower_id = p_following_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User cannot follow themselves.';
    ELSEIF is_already_following THEN
        -- Unfollow logic
        DELETE FROM Follows WHERE follower_id = p_follower_id AND following_id = p_following_id;
        -- AfterFollowDelete trigger will handle count updates.
        SELECT 'Unfollowed successfully.' AS status_message;
    ELSE
        -- Follow logic
        INSERT INTO Follows (follower_id, following_id) VALUES (p_follower_id, p_following_id);
        -- AfterFollowInsert trigger will handle count updates and notification.
        SELECT 'Followed successfully.' AS status_message;
    END IF;
END //
DELIMITER ;
```