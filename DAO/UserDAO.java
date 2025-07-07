package tracker.DAO;

import tracker.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UserDAO  {

    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static boolean saveUser(User newUser) {

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);

            String url = props.getProperty("DB_URL");
            String login = props.getProperty("DB_USER");
            String password = props.getProperty("DB_PASSWORD");

            try(Connection connection = DriverManager.getConnection(url, login, password)) {

                String hashed = encoder.encode(newUser.userPasswordProperty().get());
                String checkIfExisting = "SELECT * FROM trackuser WHERE usr_mail=?;";

                try (PreparedStatement prepIfExisting = connection.prepareStatement(checkIfExisting)) {

                    prepIfExisting.setString(1, newUser.userMailProperty().get());

                    try (ResultSet resultSet = prepIfExisting.executeQuery()){
                        // si il existe déjà un utilisateur avec le même login en base de données
                        //on redirige vers accueil
                        if (resultSet.next()) {
                            return false ;
                        } else {
                            String sqlOrder = "INSERT INTO trackuser (usr_mail, usr_password) VALUES (?, ?); ";
                            try (PreparedStatement prep = connection.prepareStatement(sqlOrder)) {
                                prep.setString(1, newUser.userMailProperty().get());
                                prep.setString(2, hashed);
                                prep.executeUpdate();
                                return true ;
                            }

                        }
                    }

                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static User isValidUser(String userMail, String userPassword) {

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);

            String url = props.getProperty("DB_URL");
            String login = props.getProperty("DB_USER");
            String password = props.getProperty("DB_PASSWORD");

            try (Connection connection = DriverManager.getConnection(url, login, password)){

                String sqlOrder = "SELECT * FROM trackuser WHERE usr_mail =?;" ;

                try (PreparedStatement prep = connection.prepareStatement(sqlOrder)){
                    prep.setString(1, userMail);

                    try(ResultSet resultSet = prep.executeQuery()){
                        if(resultSet.next()){
                            int uID = resultSet.getInt(1);
                            String hashedPassword = resultSet.getString("usr_password");
                            if (encoder.matches(userPassword, hashedPassword)){
                                User user = new User(
                                        resultSet.getString("usr_mail"),
                                        userPassword
                                );
                                user.userIdProperty().set(uID);
                                return user ;
                            }
                        }
                    }
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return  null;
    }

}
