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

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String username = userField.getText();
        String password = passwordField.getText();

        System.out.println("My email: " + username);
        System.out.println("My password: " + password);

        /* Ici, tu appelles ta méthode de vérification en base de données
        boolean isValidUser = checkCredentials(username, password);

        if (isValidUser) {
            welcomeText.setText("You have successfully logged in!");
        } else {
            welcomeText.setText("Invalid username or password.");
        } */

        // Appel provisoire direct de la "Students Display View"
        switchToStudentsDisplayView();
    }

    @FXML
    protected void onRegisterButtonClick() {
        try {
            // Load the FXML file for the Register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/register-view.fxml"));
            Parent registerRoot = fxmlLoader.load();

            // Get the current stage from a control (e.g., userField)
            Stage stage = (Stage) userField.getScene().getWindow();

            // Create a new scene with the Register view
            Scene scene = new Scene(registerRoot, 800, 600);

            // Replace the current scene with the new one
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void switchToStudentsDisplayView() {
        try {
            // Load the FXML file for the Register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/students-display-view.fxml"));
            Parent studentsDisplayRoot = fxmlLoader.load();

            // Get the current stage from a control (e.g., userField)
            Stage stage = (Stage) userField.getScene().getWindow();

            // Create a new scene with the Students Display view
            Scene scene = new Scene(studentsDisplayRoot, 800, 600);

            // Replace the current scene with the new one
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
