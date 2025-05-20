# Employee Management System - MySQL Project 📄

This project demonstrates the application of advanced database concepts by designing and implementing an Employee Management System using MySQL. Constraints, joins, and views have been utilized to ensure data integrity, retrieve complex data sets, and simplify data operations.

---

## 🚀 Database Design and Constraints

My approach to designing the `EmployeeManagement` database centered on creating a normalized structure to ensure data integrity and minimize redundancy. I identified three core entities: `Employees`, `Departments`, and `Projects`.

### Tables and Constraints:

1.  **`Departments`**:
    * `departmentId` (INT, Primary Key): Unique identifier for each department.
    * `departmentName` (VARCHAR, 100, NOT NULL): Name of the department.

2.  **`Employees`**:
    * `employeeId` (INT, Primary Key): Unique identifier for each employee.
    * `firstName` (VARCHAR, 50, NOT NULL): Employee's first name.
    * `lastName` (VARCHAR, 50, NOT NULL): Employee's last name.
    * `age` (INT): Employee's age.
    * `email` (VARCHAR, 100, UNIQUE, NOT NULL): Employee's unique email address.
    * `departmentId` (INT, Foreign Key): References `Departments(departmentId)`. Ensures an employee belongs to an existing department.

3.  **`Projects`**:
    * `projectId` (INT, Primary Key): Unique identifier for each project.
    * `projectName` (VARCHAR, 100, NOT NULL): Name of the project.
    * `projectBudget` (DECIMAL): Project's budget.
    * `managerId` (INT, Foreign Key): References `Employees(employeeId)`. Ensures the project manager is a valid employee.

**Implemented Keys and Constraints:**
* **Primary Keys (`PRIMARY KEY`)**: To uniquely identify records in each table.
* **Foreign Keys (`FOREIGN KEY`)**: To enforce referential integrity between tables (e.g., an employee must belong to an existing department, a project manager must be an existing employee).
* **`NOT NULL` Constraint**: For essential fields that must contain a value.
* **`UNIQUE` Constraint**: Applied to the `email` field in the `Employees` table to prevent duplicate email addresses.

---

## 🔗 Use of Joins

Joins are fundamental for retrieving meaningful, combined information from a relational database.

* **`INNER JOIN` for Employees and Departments**:
    To display a list of employees along with their respective department names, an `INNER JOIN` was used between the `Employees` and `Departments` tables on the condition `Employees.departmentId = Departments.departmentId`.
    ```sql
    SELECT E.firstName, E.lastName, D.departmentName
    FROM Employees E
    INNER JOIN Departments D ON E.departmentId = D.departmentId;
    ```

* **`INNER JOIN` for Projects and Managers (Employees)**:
    To show project details along with the names of their managers, an `INNER JOIN` was performed between the `Projects` and `Employees` tables on `Projects.managerId = Employees.employeeId`.
    ```sql
    SELECT P.projectName, P.projectBudget, E.firstName AS managerFirstName, E.lastName AS managerLastName
    FROM Projects P
    INNER JOIN Employees E ON P.managerId = E.employeeId;
    ```
* **Filtered Join**:
    To retrieve employees over 40 in the "Engineering" department, an `INNER JOIN` was used similar to the first example, but with an added `WHERE` clause to filter the results.

Joins allowed for the creation of comprehensive datasets by combining columns from multiple tables based on their logical relationships.

---

## 👁️ Use of Views

Views are virtual tables based on the result-set of an SQL statement. They were used to simplify data access and enhance readability/security.

* **`EmployeeDetails` View**:
    * **Definition**: This view combines `employeeId`, `firstName`, `lastName` from the `Employees` table and `departmentName` from the `Departments` table.
        ```sql
        CREATE VIEW EmployeeDetails AS
        SELECT E.employeeId, E.firstName, E.lastName, D.departmentName
        FROM Employees E
        INNER JOIN Departments D ON E.departmentId = D.departmentId;
        ```
    * **Reasoning**: This view simplifies querying common employee details. Instead of writing the join query repeatedly, users can simply execute `SELECT * FROM EmployeeDetails`.

* **`ActiveProjects` View**:
    * **Definition**: Shows `projectName`, `projectBudget`, `managerId`, and the manager's full name for all projects with a budget exceeding $1,000.
        ```sql
        CREATE VIEW ActiveProjects AS
        SELECT P.projectName, P.projectBudget, P.managerId, E.firstName AS managerFirstName, E.lastName AS managerLastName
        FROM Projects P
        JOIN Employees E ON P.managerId = E.employeeId
        WHERE P.projectBudget > 1000.00;
        ```
    * **Reasoning**: Provides a quick look at significant projects without needing to write the join and filter condition each time. It encapsulates the business logic (what constitutes an "active" project by budget) within the database.

**Views were chosen to:**
* **Simplify Complex Queries**: Reduce the need for end-users or application developers to write complicated join statements.
* **Enhance Readability**: Provide meaningful names for commonly accessed data combinations.
* **Potentially Improve Data Security**: Allow restricting access to certain columns or rows.
* **Logical Data Independence**: The underlying table structure can change, but as long as the view definition is updated to provide the same output schema, applications querying the view may not need to change.

---

## 🚧 Challenges Encountered and Resolutions

* **Ensuring Foreign Key Integrity During Data Insertion**:
    * **Challenge**: Inserting data in the wrong order can violate foreign key constraints (e.g., adding an employee to a non-existent department).
    * **Resolution**: Data was inserted in the correct order: `Departments` first, then `Employees`, and finally `Projects`. It was verified that `managerId` values in `Projects` corresponded to existing `employeeId`s.

* **Correctly Aliasing Tables in Joins**:
    * **Challenge**: Forgetting an alias or using it incorrectly can lead to errors or incorrect results.
    * **Resolution**: A consistent aliasing convention (e.g., `E` for `Employees`) was adopted, and column names were explicitly prefixed with their table alias in `SELECT`, `JOIN ON`, and `WHERE` clauses to avoid ambiguity.

* **Defining View Logic for `ActiveProjects`**:
    * **Challenge**: Deciding the criteria for "active" projects required careful consideration of the `projectBudget`.
    * **Resolution**: The `WHERE P.projectBudget > 1000.00` clause was directly incorporated into the `ActiveProjects` view definition to ensure it only displayed projects meeting this specific business rule.

---

## 💡 Key Learning Outcomes

* Understanding how to design and implement constraints to maintain data integrity.
* Practical experience using joins to combine and retrieve data across multiple tables.
* Creating views to simplify complex queries and enhance data security and readability.
* Building a real-world application that mirrors common practices in database management.

This project has reinforced skills in advanced database operations, how to organize data efficiently, and write more powerful SQL queries.