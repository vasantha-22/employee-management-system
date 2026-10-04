import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeManagementSystem {
    private static final String DB_URL =
        "jdbc:mysql://localhost:3306/employee_db";

private static final String DB_USER = "employee_app";

private static final String DB_PASSWORD = "Employee@123";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the employee database.");
            runMenu(connection);
        } catch (SQLException exception) {
            System.out.println("Could not connect to the database.");
            System.out.println("Check MySQL, database.sql, and the database credentials.");
            System.out.println("Error: " + exception.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runMenu(Connection connection) {
        int choice;

        do {
            printMenu();
            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addEmployee(connection);
                        break;
                    case 2:
                        viewEmployees(connection);
                        break;
                    case 3:
                        findEmployee(connection);
                        break;
                    case 4:
                        updateEmployee(connection);
                        break;
                    case 5:
                        deleteEmployee(connection);
                        break;
                    case 6:
                        System.out.println("Thank you for using the Employee Management System.");
                        break;
                    default:
                        System.out.println("Please choose a number from 1 to 6.");
                }
            } catch (SQLException exception) {
                System.out.println("Database operation failed: " + exception.getMessage());
            }
        } while (choice != 6);
    }

    private static void printMenu() {
        System.out.println("\n===== Employee Management System =====");
        System.out.println("1. Add employee");
        System.out.println("2. View all employees");
        System.out.println("3. Search employee by ID");
        System.out.println("4. Update employee");
        System.out.println("5. Delete employee");
        System.out.println("6. Exit");
    }

    private static void addEmployee(Connection connection) throws SQLException {
        String sql = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";

        String name = readText("Enter name: ");
        String email = readText("Enter email: ");
        String department = readText("Enter department: ");
        BigDecimal salary = readSalary();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, department);
            statement.setBigDecimal(4, salary);
            statement.executeUpdate();
            System.out.println("Employee added successfully.");
        }
    }

    private static void viewEmployees(Connection connection) throws SQLException {
        String sql = "SELECT id, name, email, department, salary FROM employees ORDER BY id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            boolean foundEmployee = false;

            System.out.printf("%-5s %-20s %-30s %-15s %-10s%n",
                    "ID", "Name", "Email", "Department", "Salary");
            System.out.println("--------------------------------------------------------------------------------");

            while (resultSet.next()) {
                foundEmployee = true;
                printEmployee(resultSet);
            }

            if (!foundEmployee) {
                System.out.println("No employees found.");
            }
        }
    }

    private static void findEmployee(Connection connection) throws SQLException {
        int id = readInt("Enter employee ID: ");
        String sql = "SELECT id, name, email, department, salary FROM employees WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    printEmployeeDetails(resultSet);
                } else {
                    System.out.println("Employee not found.");
                }
            }
        }
    }

    private static void updateEmployee(Connection connection) throws SQLException {
        int id = readInt("Enter employee ID to update: ");
        String sql = "UPDATE employees SET name = ?, email = ?, department = ?, salary = ? WHERE id = ?";

        String name = readText("Enter new name: ");
        String email = readText("Enter new email: ");
        String department = readText("Enter new department: ");
        BigDecimal salary = readSalary();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, department);
            statement.setBigDecimal(4, salary);
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

    private static void deleteEmployee(Connection connection) throws SQLException {
        int id = readInt("Enter employee ID to delete: ");
        String sql = "DELETE FROM employees WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

    private static void printEmployee(ResultSet resultSet) throws SQLException {
        System.out.printf("%-5d %-20s %-30s %-15s %-10s%n",
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("department"),
                resultSet.getBigDecimal("salary"));
    }

    private static void printEmployeeDetails(ResultSet resultSet) throws SQLException {
        System.out.println("\nEmployee details");
        System.out.println("ID: " + resultSet.getInt("id"));
        System.out.println("Name: " + resultSet.getString("name"));
        System.out.println("Email: " + resultSet.getString("email"));
        System.out.println("Department: " + resultSet.getString("department"));
        System.out.println("Salary: " + resultSet.getBigDecimal("salary"));
    }

    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static BigDecimal readSalary() {
        while (true) {
            try {
                System.out.print("Enter salary: ");
                BigDecimal salary = new BigDecimal(scanner.nextLine().trim());
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Salary cannot be negative.");
                } else {
                    return salary;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid salary.");
            }
        }
    }
}
