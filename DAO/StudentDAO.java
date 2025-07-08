package tracker.DAO;

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

    public static ArrayList<Student> selectAll(){

        Properties props = new Properties();
        try(FileInputStream fis = new FileInputStream("application.properties")){
            props.load((fis));

            String url = props.getProperty("DB_URL");
            String login = props.getProperty("DB_USER");
            String password = props.getProperty("DB_PASSWORD");

            try(Connection connection =DriverManager.getConnection(url, login, password)){

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
                        Float averageGrade =rset.getFloat("stud_average_grade");

                        Student newStudent = new Student(firstName, lastName, birthDate, studentNumber, educationLevel, averageGrade);
                        studentsList.add(newStudent);
                    }
                    return  studentsList;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch ( Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
