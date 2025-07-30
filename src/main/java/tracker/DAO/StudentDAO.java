package tracker.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tracker.model.DatabaseConnection;
import tracker.model.Student;

/**
 * Data Access Object (DAO) for handling operations related to Student entities.
 * This class provides methods for selecting, inserting, deleting, and updating student records.
 */
public class StudentDAO {

    /**
     * Retrieves all students from the database.
     *
     * @return a list of all students
     * @throws SQLException if a database access error occurs
     */
    public static ArrayList<Student> selectAll() throws SQLException {
        String sqlQuery = "SELECT stud_first_name, stud_last_name, stud_birth_date, stud_number, stud_level, stud_average_grade FROM trackstudent;";
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
     * Finds a student by their student number.
     *
     * @param studentNumber the student number to search for
     * @return the matching Student object, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public static Student findByStudentNumber(String studentNumber) throws SQLException {
        String sql = "SELECT stud_first_name, stud_last_name, stud_birth_date, stud_level, stud_average_grade FROM trackstudent WHERE stud_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("stud_first_name");
                String lastName = rs.getString("stud_last_name");
                Date birthDate = rs.getDate("stud_birth_date");
                String educationLevel = rs.getString("stud_level");
                double averageGrade = rs.getDouble("stud_average_grade");

                return new Student(firstName, lastName, birthDate.toLocalDate(),
                        studentNumber, educationLevel, averageGrade);
            } else {
                return null;
            }
        }
    }

    /**
     * Inserts a new student into the database.
     *
     * @param student the Student object to insert
     * @throws SQLException if a database access error occurs
     */
    public static void insertStudent(Student student) throws SQLException {
        String sql = "INSERT INTO trackstudent (stud_number, stud_first_name, stud_last_name, stud_birth_date, " +
                "stud_level, stud_average_grade) " +
                "VALUES (?, ?, ?, ?, ?::level, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setDate(4, Date.valueOf(student.getBirthDate()));
            stmt.setString(5, student.getEducationLevel());
            stmt.setDouble(6, student.getAverageGrade());

            stmt.executeUpdate();
            System.out.println("Student inserted successfully.");
        }
    }

    public static boolean updateStudent(String originalStudentNumber, Student student) throws SQLException {
        String sql = "UPDATE trackstudent SET " +
                "stud_number = ?, " +
                "stud_first_name = ?, " +
                "stud_last_name = ?, " +
                "stud_birth_date = ?, " +
                "stud_level = ?::level, " +
                "stud_average_grade = ? " +
                "WHERE stud_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setDate(4, Date.valueOf(student.getBirthDate()));
            stmt.setString(5, student.getEducationLevel());
            stmt.setDouble(6, student.getAverageGrade());
            // Original student number (used as key), even if student number is changed
            stmt.setString(7, originalStudentNumber);


            int affectedRows = stmt.executeUpdate();
            System.out.println("Student updated successfully.");
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

}
