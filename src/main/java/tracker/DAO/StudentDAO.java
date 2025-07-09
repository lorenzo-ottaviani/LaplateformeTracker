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

}
