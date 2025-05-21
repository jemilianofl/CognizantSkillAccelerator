-- 4. Advanced Topics (Examples) ⚙️
-- ---
-- Triggers

-- Automatically update Posts.like_count when a like is added or removed.
-- Trigger for adding a like
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

-- Trigger for removing a like
DELIMITER //
CREATE TRIGGER AfterLikeDelete_UpdatePostLikeCount
AFTER DELETE ON Likes
FOR EACH ROW
BEGIN
    UPDATE Posts SET like_count = GREATEST(0, like_count - 1) WHERE post_id = OLD.post_id;
END //
DELIMITER ;

-- Automatically update Users.follower_count and Users.following_count when a follow is added or removed.
-- Trigger for adding a follow
DELIMITER //
CREATE TRIGGER AfterFollowInsert_UpdateUserCountsAndNotify
AFTER INSERT ON Follows
FOR EACH ROW
BEGIN
    DECLARE follower_username VARCHAR(50);

    -- Update follow counts
    UPDATE Users SET following_count = following_count + 1 WHERE user_id = NEW.follower_id;
    UPDATE Users SET follower_count = follower_count + 1 WHERE user_id = NEW.following_id;

    -- Get the username of the follower
    SELECT username INTO follower_username FROM Users WHERE user_id = NEW.follower_id;

    -- Create notification for the user being followed
    INSERT INTO Notifications (user_id, actor_id, notification_type, notification_text, target_url)
    VALUES (NEW.following_id, NEW.follower_id, 'follow',
            CONCAT(follower_username, ' started following you.'),
            CONCAT('/profile/', NEW.follower_id));
END //
DELIMITER ;

-- Trigger for removing a follow (unfollowing)
DELIMITER //
CREATE TRIGGER AfterFollowDelete_UpdateUserCounts
AFTER DELETE ON Follows
FOR EACH ROW
BEGIN
    UPDATE Users SET following_count = GREATEST(0, following_count - 1) WHERE user_id = OLD.follower_id;
    UPDATE Users SET follower_count = GREATEST(0, follower_count - 1) WHERE user_id = OLD.following_id;
END //
DELIMITER ;

-- Automatically update Users.post_count when a post is created.
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

-- Automatically update Posts.comment_count when a comment is added/deleted and notify.
DELIMITER //
CREATE TRIGGER AfterCommentInsert_UpdatePostCommentCountAndNotify
AFTER INSERT ON Comments
FOR EACH ROW
BEGIN
    DECLARE post_author_id INT;
    DECLARE commenter_username VARCHAR(50);

    UPDATE Posts SET comment_count = comment_count + 1 WHERE post_id = NEW.post_id;

    SELECT user_id INTO post_author_id FROM Posts WHERE post_id = NEW.post_id;
    SELECT username INTO commenter_username FROM Users WHERE user_id = NEW.user_id;

    IF post_author_id != NEW.user_id THEN
        INSERT INTO Notifications (user_id, actor_id, post_id, notification_type, notification_text, target_url)
        VALUES (post_author_id, NEW.user_id, NEW.post_id, 'comment',
                CONCAT(commenter_username, ' commented on your post.'),
                CONCAT('/posts/', NEW.post_id, '#comment-', NEW.comment_id));
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER AfterCommentDelete_UpdatePostCommentCount
AFTER DELETE ON Comments
FOR EACH ROW
BEGIN
    UPDATE Posts SET comment_count = GREATEST(0, comment_count - 1) WHERE post_id = OLD.post_id;
END //
DELIMITER ;

-- Automatically notify users of new messages.
DELIMITER //
CREATE TRIGGER AfterMessageInsert_NotifyReceiver
AFTER INSERT ON Messages
FOR EACH ROW
BEGIN
    DECLARE sender_username VARCHAR(50);
    SELECT username INTO sender_username FROM Users WHERE user_id = NEW.sender_id;

    INSERT INTO Notifications (user_id, actor_id, message_id, notification_type, notification_text, target_url)
    VALUES (NEW.receiver_id, NEW.sender_id, NEW.message_id, 'message',
            CONCAT('You have a new message from ', sender_username, '.'),
            CONCAT('/messages/thread/', NEW.sender_id)); -- Assuming thread URL based on other user
END //
DELIMITER ;

-- Stored Procedures

-- Generate personalized follow recommendations (simplified example).
-- This procedure suggests following friends of friends (users followed by people the current user already follows).
DELIMITER //
CREATE PROCEDURE GetFollowRecommendations(IN current_user_id INT, IN limit_count INT)
BEGIN
    SELECT DISTINCT
        u.user_id,
        u.username,
        u.profile_picture_url,
        u.bio,
        (SELECT COUNT(*) FROM Follows f_mutual WHERE f_mutual.follower_id = current_user_id AND f_mutual.following_id IN (SELECT f2.follower_id FROM Follows f2 WHERE f2.following_id = u.user_id)) AS mutual_friends_following_them
    FROM Users u
    JOIN Follows f1 ON u.user_id = f1.following_id -- u is someone who is followed
    JOIN Follows f2 ON f1.follower_id = f2.follower_id -- f1.follower_id is a "friend" of the current user
    WHERE f2.following_id = current_user_id -- f2.follower_id is followed by the current user (is a "friend")
      AND u.user_id != current_user_id -- Don't recommend oneself
      AND u.user_id NOT IN (SELECT following_id FROM Follows WHERE follower_id = current_user_id) -- Don't recommend those already followed
    GROUP BY u.user_id, u.username, u.profile_picture_url, u.bio
    ORDER BY mutual_friends_following_them DESC, RAND() -- Prioritize by "friends of friends" then random
    LIMIT limit_count;
END //
DELIMITER ;

-- How to call it:
-- CALL GetFollowRecommendations(1, 5); -- Get 5 recommendations for user with user_id = 1

-- Procedure to follow/unfollow a user (handles insertion/deletion logic).
-- (Note: With the Follows triggers already defined for count updates, this procedure is more for encapsulating the action)
DELIMITER //
CREATE PROCEDURE ToggleFollowUser(IN p_follower_id INT, IN p_following_id INT)
BEGIN
    DECLARE already_following BOOLEAN;
    SELECT EXISTS(SELECT 1 FROM Follows WHERE follower_id = p_follower_id AND following_id = p_following_id) INTO already_following;

    IF p_follower_id = p_following_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User cannot follow themselves.';
    ELSEIF already_following THEN
        -- Unfollow
        DELETE FROM Follows WHERE follower_id = p_follower_id AND following_id = p_following_id;
        -- AfterFollowDelete triggers will handle counts and notifications (if applicable)
        SELECT 'Unfollowed' AS status;
    ELSE
        -- Follow
        INSERT INTO Follows (follower_id, following_id) VALUES (p_follower_id, p_following_id);
        -- AfterFollowInsert triggers will handle counts and notifications
        SELECT 'Followed' AS status;
    END IF;
END //
DELIMITER ;

-- How to call it:
-- CALL ToggleFollowUser(1, 4); -- User 1 follows/unfollows user 4