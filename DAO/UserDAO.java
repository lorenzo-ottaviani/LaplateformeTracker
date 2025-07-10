package tracker.DAO;

import tracker.model.DatabaseConnection;
import tracker.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO  {

    static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Saves a new user in the database.
     * @param newUser the user to save
     * @return true if saved successfully; false if email already exists.
     * @throws SQLException if a database error occurs
     */
    public static boolean saveUser(User newUser) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String email = newUser.userMailProperty().get();
            String hashedPassword = encoder.encode(newUser.userPasswordProperty().get());

            String checkQuery = "SELECT 1 FROM trackuser WHERE usr_mail = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, email);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        return false; // user exists
                    }
                }
            }

            String insertQuery = "INSERT INTO trackuser (usr_mail, usr_password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, email);
                insertStmt.setString(2, hashedPassword);
                insertStmt.executeUpdate();
                return true;
            }
        }
    }

    /**
     * Attempts to authenticate a user.
     * @return a User object if credentials are valid; otherwise null.
     * @throws SQLException if a DB error occurs
     */
    public static User loginUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM trackuser WHERE usr_mail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("usr_password");
                    if (encoder.matches(password, hashed)) {
                        User user = new User(rs.getString("usr_mail"), password);
                        user.userIdProperty().set(rs.getInt("usr_id"));
                        return user;
                    }
                }
            }
        }

        return null;
    }

}
