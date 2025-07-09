package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tracker.DAO.StudentDAO;
import tracker.model.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DeleteConfirmController implements Initializable {

    @FXML
    private Label studentNameLabel;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private Student student;

    private StudentsDisplayController displayController;

    public void setStudent(Student student, StudentsDisplayController controller) {
        this.student = student;
        this.displayController = controller;
        studentNameLabel.setText("Voulez-vous supprimer " + student.getStudentNumber() + " ?");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmButton.setOnAction(e -> {
            try {
                boolean deleted = StudentDAO.deleteStudent(student);
                if (deleted && displayController != null) {
                    displayController.refreshStudentTable();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(event -> closeWindow());
    }

    private void handleDelete() {
        if (student != null) {
            try {
                boolean success = StudentDAO.deleteStudent(student);
                if (success) {
                    showAlert("Succès", "L'élève a été supprimé.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Échec de la suppression de l'élève.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Exception", e.getMessage(), Alert.AlertType.ERROR);
            }
            closeWindow();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
