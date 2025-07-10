package tracker.DAO;

import tracker.model.DatabaseConnection;
import tracker.model.Student;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.Date;

public class StudentDAO {

    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static ArrayList<Student> selectAll() throws SQLException {

        try(Connection connection = DatabaseConnection.getConnection()){
            String sqlOrder = "SELECT * FROM trackstudent;";
            ArrayList<Student> studentsList = new ArrayList<>();

            try(Statement stat = connection.createStatement()){
                ResultSet rset = stat.executeQuery(sqlOrder);
                while (rset.next()){
                    String firstName = rset.getString("stud_first_name");
                    String lastName = rset.getString("stud_last_name");
                    Date birthDate = rset.getDate("stud_birth_date");
                    String studentNumber = rset.getString("stud_number");
                    String educationLevel = rset.getString("stud_level");
                    Double averageGrade =rset.getDouble("stud_average_grade");

                    Student newStudent = new Student(firstName, lastName, birthDate.toLocalDate(), studentNumber,
                            educationLevel, averageGrade);
                    studentsList.add(newStudent);
                }
                return  studentsList;
            }
        }
    }

    public static boolean deleteStudent(Student student) throws SQLException {
        String sql = "DELETE FROM trackstudent WHERE stud_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getStudentNumber());
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean updateFirstNameStudent(String studentID, String newFirstName) throws  SQLException{
        try(Connection conn = DatabaseConnection.getConnection()){
            String sqlOrder = "UPDATE trackstudent SET stud_first_name=? WHERE stud_number =?;";
            try(PreparedStatement prep = conn.prepareStatement(sqlOrder)){
                prep.setString(1, newFirstName);
                prep.setString(2, studentID);
                prep.executeUpdate();
                System.out.println("First name has been changed");
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    return false;
    }

public static  boolean updateLastNameStudent(String studentID, String newLastName) throws  SQLException{
    try(Connection conn = DatabaseConnection.getConnection()) {
        String sqlOrder = "UPDATE trackstudent SET stud_last_name=? WHERE stud_number =?;";
        try(PreparedStatement prep = conn.prepareStatement(sqlOrder)){
            prep.setString(1, newLastName);
            prep.setString(2, studentID);
            prep.executeUpdate();
            System.out.println("First name has been changed");
            return true;
        }
    } catch(SQLException e) {
        e.printStackTrace();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return false;
    }

}
