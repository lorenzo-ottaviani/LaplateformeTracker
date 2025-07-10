package tracker.DAO;

import tracker.model.DatabaseConnection;
import tracker.model.Student;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for handling operations related to Student entities.
 * This class provides methods for selecting, deleting, and updating student records.
 */
public class StudentDAO {

    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Retrieves all students from the database.
     *
     * @return a list of all students
     * @throws SQLException if a database access error occurs
     */
    public static ArrayList<Student> selectAll() throws SQLException {
        String sqlQuery = "SELECT * FROM trackstudent;";
        ArrayList<Student> studentsList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("stud_first_name");
                String lastName = resultSet.getString("stud_last_name");
                Date birthDate = resultSet.getDate("stud_birth_date");
                String studentNumber = resultSet.getString("stud_number");
                String educationLevel = resultSet.getString("stud_level");
                double averageGrade = resultSet.getDouble("stud_average_grade");

                Student student = new Student(firstName, lastName, birthDate.toLocalDate(),
                        studentNumber, educationLevel, averageGrade);
                studentsList.add(student);
            }
        }

        return studentsList;
    }

    /**
     * Deletes a student from the database based on their student number.
     *
     * @param student the student to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean deleteStudent(Student student) throws SQLException {
        String sql = "DELETE FROM trackstudent WHERE stud_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentNumber());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates a student's first name in the database.
     *
     * @param studentID the student number
     * @param newFirstName the new first name to set
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean updateFirstNameStudent(String studentID, String newFirstName) throws SQLException {
        String sql = "UPDATE trackstudent SET stud_first_name = ? WHERE stud_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newFirstName);
            stmt.setString(2, studentID);
            stmt.executeUpdate();
            System.out.println("First name updated successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a student's last name in the database.
     *
     * @param studentID the student number
     * @param newLastName the new last name to set
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean updateLastNameStudent(String studentID, String newLastName) throws SQLException {
        String sql = "UPDATE trackstudent SET stud_last_name = ? WHERE stud_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newLastName);
            stmt.setString(2, studentID);
            stmt.executeUpdate();
            System.out.println("Last name updated successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
