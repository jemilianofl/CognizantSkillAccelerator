-- 1. Database Schema 🧱

CREATE DATABASE IF NOT EXISTS SocialMediaPlatform;
USE SocialMediaPlatform;

-- Users Table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Store hashed passwords
    date_of_birth DATE,
    profile_picture_url VARCHAR(255),
    bio TEXT,
    post_count INT DEFAULT 0,
    follower_count INT DEFAULT 0,
    following_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Posts Table
CREATE TABLE Posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    post_text TEXT,
    media_url VARCHAR(255), -- URL for image or video
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from post_date
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Comments Table
CREATE TABLE Comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from comment_date
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Likes Table
CREATE TABLE Likes (
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from like_date
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_post_like (user_id, post_id) -- A user can only like a post once
);

-- Follows Table
CREATE TABLE Follows (
    follow_id INT AUTO_INCREMENT PRIMARY KEY,
    follower_id INT NOT NULL, -- The user who is following
    following_id INT NOT NULL, -- The user who is being followed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from follow_date
    FOREIGN KEY (follower_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_follow_pair (follower_id, following_id) -- Prevents duplicate follow entries
);

-- Messages Table
CREATE TABLE Messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from message_date
    FOREIGN KEY (sender_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Notifications Table
CREATE TABLE Notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL, -- The user who receives the notification
    actor_id INT, -- The user who performed the action (can be NULL)
    post_id INT, -- Optional, if the notification is related to a post
    message_id INT, -- Optional, if the notification is related to a message
    notification_type ENUM('like', 'comment', 'follow', 'message', 'mention', 'system') NOT NULL,
    notification_text VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    target_url VARCHAR(255), -- URL to navigate to when the notification is clicked
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Changed from notification_date
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES Users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES Posts(post_id) ON DELETE SET NULL,
    FOREIGN KEY (message_id) REFERENCES Messages(message_id) ON DELETE SET NULL
);