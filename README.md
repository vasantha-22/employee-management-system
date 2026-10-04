# Employee Management System

A beginner-friendly console application built with Java, JDBC, and MySQL.

## Features

- Add an employee
- View all employees
- Search an employee by ID
- Update an employee
- Delete an employee
- Exit from the menu

## Requirements

- Java 17 or newer
- Maven
- MySQL Server

## Setup

1. Start MySQL Server.
2. Run the statements in `database.sql` using MySQL Workbench or the MySQL command line.
3. Open `src/main/java/EmployeeManagementSystem.java` and update these constants if needed:
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
4. Compile and run the application:

```text
mvn compile exec:java
```

The program uses a `PreparedStatement` for every database operation. This keeps the SQL easy to read and avoids putting user input directly into SQL statements.
