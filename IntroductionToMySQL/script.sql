CREATE DATABASE StudentRecords;
SHOW DATABASES;
USE StudentRecords;
CREATE TABLE Students (
    studentId INT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    age INT,
    email VARCHAR(100)
);
INSERT INTO Students (studentId, firstName, lastName, age, email) VALUES
(1, 'John', 'Doe', 20, 'john.doe@example.com'),
(2, 'Jane', 'Smith', 22, 'jane.smith@example.com'),
(3, 'Mike', 'Johnson', 19, 'mike.johnson@example.com'),
(4, 'Emily', 'Davis', 21, 'emily.davis@example.com'),
(5, 'Chris', 'Brown', 23, 'chris.brown@example.com');
CREATE TABLE Courses (
    courseId INT PRIMARY KEY,
    courseName VARCHAR(100),
    courseDescription TEXT
);
CREATE TABLE Enrollments (
    enrollmentId INT PRIMARY KEY AUTO_INCREMENT,
    studentId INT,
    courseId INT,
    FOREIGN KEY (studentId) REFERENCES Students(studentId),
    FOREIGN KEY (courseId) REFERENCES Courses(courseId)
);

ALTER TABLE Courses
ADD COLUMN studentId INT;

-- Optionally, to make it a foreign key if it references the Students table:
ALTER TABLE Courses
ADD CONSTRAINT FK_CourseStudent FOREIGN KEY (studentId) REFERENCES Students(studentId);

-- Insert data 
INSERT INTO Students (studentId, firstName, lastName, age, email) VALUES
(6, 'Sarah', 'Wilson', 20, 'sarah.wilson@example.com'),
(7, 'David', 'Lee', 22, 'david.lee@example.com');

-- Select data
SELECT * FROM Students;

-- Update data
UPDATE Students SET age = 21 WHERE studentId = 1;

-- Verify changes
SELECT * FROM Students WHERE studentId = 1;

-- See all students
SELECT * FROM Students;
