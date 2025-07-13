package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tracker.DAO.UserDAO;
import tracker.model.User;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller responsible for handling user interactions in the login view.
 * Allows users to log in or navigate to the registration view.
 */
public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    /**
     * Triggered when the login button is clicked.
     * Attempts to authenticate the user with the provided credentials.
     * Displays an alert if login fails or if a database error occurs.
     */
    @FXML
    protected void onLoginButtonClick() {
        String email = userField.getText();
        String password = passwordField.getText();

        try {
            User user = UserDAO.loginUser(email, password);

            if (user != null) {
                System.out.println("✅ Login successful.");
                switchToStudentsDisplayView();
            } else {
                showAlert(Alert.AlertType.WARNING, "Login Failed", "Invalid email or password.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error during login: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the database.");
        }
    }

    /**
     * Triggered when the register button is clicked.
     * Loads the registration view (register-view.fxml).
     */
    @FXML
    protected void onRegisterButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/register-view.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage stage = (Stage) userField.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(registerRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the registration screen.");
        }
    }

    /**
     * Switches the current scene to the Students Display view (students-display-view.fxml).
     * Called after a successful login.
     */
    protected void switchToStudentsDisplayView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/students-display-view.fxml"));
            Parent studentsDisplayRoot = fxmlLoader.load();

            Stage stage = (Stage) userField.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(studentsDisplayRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the student dashboard.");
        }
    }

    /**
     * Utility method to show a JavaFX alert dialog.
     *
     * @param type    The type of alert (e.g., ERROR, WARNING, INFORMATION)
     * @param title   The title of the alert dialog
     * @param message The message content shown in the alert
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
