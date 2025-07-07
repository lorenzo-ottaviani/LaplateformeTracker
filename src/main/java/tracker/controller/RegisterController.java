package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordFailed;

    @FXML
    private Label registrationErrorLabel;

    @FXML
    protected void onLoginButtonClick() {
        try {
            // Load the FXML file for the Login view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Get the current stage from a control (ex: backToLoginButton)
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Create a new scene with the Login view
            Scene scene = new Scene(loginRoot, 800, 600);

            // Replace the current scene with the new one
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRegisterButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        System.out.println("My email: " + email);
        System.out.println("My password (first entry): " + password);
        System.out.println("My password (confirmation): " + confirmPassword);

        // Email validation: must contain '@' and a '.' after the '@' part
        if (email == null || !email.matches("^.+@.+\\..+$")) {
            registrationErrorLabel.setText("Invalid email address.");
            registrationErrorLabel.setVisible(true);
            return; // Stop here if email is invalid
        }

        // Password strength validation
        if (!isValidPassword(password)) {
            registrationErrorLabel.setText("Password must be at least 12 characters and include uppercase, lowercase, " +
                    "digit, and special character.");
            registrationErrorLabel.setVisible(true);
            return;
        }

        // Password matching check
        if (!password.equals(confirmPassword)) {
            registrationErrorLabel.setText("Passwords do not match.");
            registrationErrorLabel.setVisible(true);
        } else {
            registrationErrorLabel.setVisible(false);
            System.out.println("Email and passwords are valid! You can register the user.");
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 12) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

}
