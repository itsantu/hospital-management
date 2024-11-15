import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("Enter patient name: ");
        scanner.nextLine(); // Consume the newline left-over from previous input
        String name = scanner.nextLine(); // Read the entire line for the name
    
        System.out.print("Enter patient age: ");
        while (!scanner.hasNextInt()) { // Check if the next input is an integer
            System.out.println("Invalid input. Please enter a valid age (integer): ");
            scanner.next(); // Consume the invalid input
        }
        int age = scanner.nextInt(); // Read the integer age
    
        System.out.print("Enter patient gender: ");
        String gender = scanner.next(); // This could also be nextLine() if gender can have spaces
    
        try {
            String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
    
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient!");
            }
        } catch (SQLException e) {
            e.printStackTrace();    
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients:");
            System.out.println("+--------+----------------------+---------+----------+");
            System.out.println("|   ID   |     Patient Name     |   Age   |  Gender  |");
            System.out.println("+--------+----------------------+---------+----------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-8s|%-22s|%-9s|%-10s|\n", id, name, age, gender);                
                System.out.println("+--------+----------------------+---------+----------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById (int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
