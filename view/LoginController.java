package tracker.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tracker.DAO.StudentDAO;
import tracker.DAO.UserDAO;
import tracker.model.Student;
import tracker.model.User;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String email = userField.getText();
        String password = passwordField.getText();

        System.out.println("My email: " + email);
        System.out.println("My password: " + password);

        User user = UserDAO.isValidUser(email, password);

        // seuls les utilisateurs enregistrés peuvent accéder aux étudiants
        if (user !=null){
            System.out.println("félicitations vous êtes connecté !");
            switchToStudentsDisplayView();
        }
        else {
            System.out.println("mauvais utilisateur");
        }

    }

    @FXML
    protected void onRegisterButtonClick() {
        try {
            // Load the FXML file for the Register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("students-display-view.fxml"));
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
