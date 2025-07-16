package tracker.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import tracker.DAO.UserDAO;
import tracker.model.User;

import java.io.IOException;

/**
 * Controller for the registration view (register-view.fxml).
 * Handles input validation, user registration, and navigation between views.
 */
public class RegisterController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label registrationErrorLabel;

    /**
     * Handles the event when the user clicks the "Back to Login" button.
     * Loads the login view and displays it.
     */
    @FXML
    protected void onLoginButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(loginRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the user clicks the "Register" button.
     * Validates the email and passwords, then attempts to register the user.
     * Displays appropriate messages on success or failure.
     */
    @FXML
    protected void onRegisterButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        registrationErrorLabel.setVisible(false);

        if (!validateEmailAndPassword(email, password, confirmPassword)) {
            registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;");
            registrationErrorLabel.setVisible(true);
            return;
        }

        User newUser = new User(email, password);

        try {
            if (UserDAO.saveUser(newUser)) {
                newUser = UserDAO.loginUser(email, password);

                registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2a9d8f;"); // green
                registrationErrorLabel.setText("Registration successful! Redirecting to login...");
                registrationErrorLabel.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> onLoginButtonClick());
                pause.play();

            } else {
                registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;"); // red
                registrationErrorLabel.setText("Registration failed: Email may already be in use.");
                registrationErrorLabel.setVisible(true);
            }
        } catch (Exception e) {
            registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;"); // red
            registrationErrorLabel.setText("An error occurred during registration. Please try again.");
            registrationErrorLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    /**
     * Validates the email format and password strength, including password confirmation.
     *
     * @param email User's email address.
     * @param password User's chosen password.
     * @param confirmPassword Confirmation of the password.
     * @return true if all validations pass, false otherwise.
     */
    private boolean validateEmailAndPassword(String email, String password, String confirmPassword) {
        if (email == null || !email.matches("^.+@.+\\..+$")) {
            registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;");
            registrationErrorLabel.setText("Invalid email address.");
            registrationErrorLabel.setVisible(true);
            return false;
        }

        if (!isValidPassword(password)) {
            registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;");
            registrationErrorLabel.setText("Password must be at least 12 characters and include uppercase, lowercase, " +
                    "digit, and special character.");
            registrationErrorLabel.setVisible(true);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            registrationErrorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #e63946;");
            registrationErrorLabel.setText("Passwords do not match.");
            registrationErrorLabel.setVisible(true);
            return false;
        }

        return true;
    }

    /**
     * Checks if the password meets strength requirements:
     * minimum length 12, contains uppercase, lowercase, digit, and special character.
     *
     * @param password Password to validate.
     * @return true if password is strong enough, false otherwise.
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 12) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

}
