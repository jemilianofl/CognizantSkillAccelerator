-- Step 1: Database Design
-- Create a Database
CREATE DATABASE EmployeeManagement;
USE EmployeeManagement;

-- Create Tables with Constraints
-- Departments Table
CREATE TABLE Departments (
    departmentId INT PRIMARY KEY,
    departmentName VARCHAR(100) NOT NULL
);

-- Employees Table
CREATE TABLE Employees (
    employeeId INT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    age INT,
    email VARCHAR(100) UNIQUE NOT NULL,
    departmentId INT,
    CONSTRAINT FK_Department FOREIGN KEY (departmentId) REFERENCES Departments(departmentId)
);

-- Projects Table
CREATE TABLE Projects (
    projectId INT PRIMARY KEY,
    projectName VARCHAR(100) NOT NULL,
    projectBudget DECIMAL(10, 2),
    managerId INT,
    CONSTRAINT FK_Manager FOREIGN KEY (managerId) REFERENCES Employees(employeeId)
);