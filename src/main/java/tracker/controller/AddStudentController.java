package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddStudentController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDateField;
    @FXML private TextField studentNumberField;
    @FXML private TextField educationLevelField;
    @FXML private TextField averageGradeField;

    @FXML private Label addErrorLabel;

    @FXML
    protected void onAddStudentButtonClick() {

    }

    /**
     * Handles the Back button click event.
     * Switches the current scene to the Students Display view.
     */
    @FXML
    protected void onBackButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/students-display-view.fxml"));
            Parent studentsDisplayRoot = fxmlLoader.load();

            Stage stage = (Stage) firstNameField.getScene().getWindow();
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
