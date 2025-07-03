package tracker.view;

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

        /* Ici, tu appelles ta méthode de vérification en base de données
        boolean isValidUser = checkCredentials(username, password);

        if (isValidUser) {
            welcomeText.setText("You have successfully logged in!");
        } else {
            welcomeText.setText("Invalid username or password.");
        } */
    }
}
