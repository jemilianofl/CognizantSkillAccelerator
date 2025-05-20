-- Step 2: Data Manipulation and Retrieval
-- Insert Sample Data

-- Departments
INSERT INTO Departments (departmentId, departmentName) VALUES
(1, 'Human Resources'),
(2, 'Engineering'),
(3, 'Marketing'),
(4, 'Sales');

-- Employees
INSERT INTO Employees (employeeId, firstName, lastName, age, email, departmentId) VALUES
(1, 'Alice', 'Johnson', 30, 'alice.johnson@example.com', 1),
(2, 'Bob', 'Smith', 45, 'bob.smith@example.com', 2),
(3, 'Charlie', 'Brown', 28, 'charlie.brown@example.com', 2),
(4, 'Diana', 'Davis', 52, 'diana.davis@example.com', 3),
(5, 'Edward', 'Wilson', 35, 'edward.wilson@example.com', 2),
(6, 'Fiona', 'Clark', 41, 'fiona.clark@example.com', 4),
(7, 'George', 'Miller', 38, 'george.miller@example.com', 1);

-- Projects
-- Ensure managerId exists in Employees table
INSERT INTO Projects (projectId, projectName, projectBudget, managerId) VALUES
(101, 'Website Redesign', 5000.00, 2),
(102, 'Mobile App Development', 12000.00, 5),
(103, 'Marketing Campaign Q3', 750.00, 4),
(104, 'New CRM Implementation', 25000.00, 2),
(105, 'Employee Training Program', 950.00, 1);

-- Write SQL Queries Using Joins

-- Retrieve Employee Information: Display the list of all employees along with their department names.
SELECT E.firstName, E.lastName, D.departmentName
FROM Employees E
INNER JOIN Departments D ON E.departmentId = D.departmentId;

-- Project and Manager Information: Show all projects along with the names of the managers assigned to them.
SELECT P.projectName, P.projectBudget, E.firstName AS managerFirstName, E.lastName AS managerLastName
FROM Projects P
INNER JOIN Employees E ON P.managerId = E.employeeId;

-- Filter Data Using WHERE: Retrieve a list of employees over the age of 40 who work in the Engineering department.
SELECT E.firstName, E.lastName, E.age, D.departmentName
FROM Employees E
INNER JOIN Departments D ON E.departmentId = D.departmentId
WHERE E.age > 40 AND D.departmentName = 'Engineering';

-- Create Views to Simplify Data Access

-- Employee Details View:
-- Create a view named EmployeeDetails that shows employeeId, firstName, lastName, and departmentName.
CREATE VIEW EmployeeDetails AS
SELECT E.employeeId, E.firstName, E.lastName, D.departmentName
FROM Employees E
INNER JOIN Departments D ON E.departmentId = D.departmentId;

-- To query the view:
-- SELECT * FROM EmployeeDetails;

-- Active Projects View:
-- Create a view named ActiveProjects that shows projectName, projectBudget, and managerId for all projects with a budget over $1,000.
CREATE VIEW ActiveProjects AS
SELECT P.projectName, P.projectBudget, P.managerId, E.firstName AS managerFirstName, E.lastName AS managerLastName
FROM Projects P
JOIN Employees E ON P.managerId = E.employeeId
WHERE P.projectBudget > 1000.00;

-- To query the view:
SELECT * FROM ActiveProjects;