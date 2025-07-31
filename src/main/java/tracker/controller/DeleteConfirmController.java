package tracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import tracker.DAO.StudentDAO;
import tracker.model.Student;

/**
 * Controller class for the delete confirmation dialog.
 * It handles user confirmation before deleting a student and notifies the main display controller to refresh.
 */
public class DeleteConfirmController implements Initializable {

    private Student student;
    private StudentsDisplayController displayController;

    @FXML private Label studentNameLabel;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    /**
     * Sets the student to be deleted and the controller that needs to be refreshed after deletion.
     *
     * @param student the student to be deleted
     * @param controller the controller to be refreshed after deletion
     */
    public void setStudent(Student student, StudentsDisplayController controller) {
        this.student = student;
        this.displayController = controller;
        studentNameLabel.setText("Do you want to delete the student \""
                + student.firstNameProperty().get() + " "
                + student.lastNameProperty().get()
                + "\" with student number '"
                + student.getStudentNumber() + "'?");
    }

    /**
     * Initializes the controller by setting up button actions.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmButton.setOnAction(e -> handleDelete());
        cancelButton.setOnAction(e -> closeWindow());
    }

    /**
     * Handles the deletion of the student.
     * It shows a success or error alert depending on the outcome and refreshes the display if needed.
     */
    private void handleDelete() {
        if (student != null) {
            try {
                boolean success = StudentDAO.deleteStudent(student);
                if (success) {
                    showAlert("Success", "Student was successfully deleted.", Alert.AlertType.INFORMATION);
                    if (displayController != null) {
                        displayController.refreshStudentTable();
                    }
                } else {
                    showAlert("Error", "Failed to delete student.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("SQL Error", e.getMessage(), Alert.AlertType.ERROR);
            } finally {
                closeWindow();
            }
        }
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an alert with the specified parameters.
     *
     * @param title the title of the alert
     * @param content the content/message of the alert
     * @param type the type of the alert (INFORMATION, ERROR, etc.)
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
