package tracker.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String username = userField.getText();
        String password = passwordField.getText();

        /* Ici, tu appelles ta méthode de vérification en base de données
        boolean isValidUser = checkCredentials(username, password);

        if (isValidUser) {
            welcomeText.setText("You have successfully logged in!");
        } else {
            welcomeText.setText("Invalid username or password.");
        } */
    }

    @FXML
    protected void onRegisterButtonClick() {}
}