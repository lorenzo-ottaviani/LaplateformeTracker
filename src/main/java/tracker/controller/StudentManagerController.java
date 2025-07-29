package tracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import tracker.DAO.StudentDAO;
import tracker.model.Student;

public class StudentManagerController implements Initializable {

    // === Internal data ===
    private Student student;

    // === FXML Labels ===
    @FXML private Label colFirstNameLabel;
    @FXML private Label colLastNameLabel;
    @FXML private Label colBirthDateLabel;
    @FXML private Label colStudentNumberLabel;
    @FXML private Label colEducationLevelLabel;
    @FXML private Label colAverageGradeLabel;

    // === FXML Input fields ===
    @FXML private TextField inputFirstName;
    @FXML private TextField inputLastName;
    @FXML private DatePicker inputBirthDate;
    @FXML private TextField inputStudentNumber;
    @FXML private TextField inputEducationLevel;
    @FXML private TextField inputAverageGrade;

    public void setStudentData(Student student) {
        this.student = student;

        // Display current values in labels using getters
        colFirstNameLabel.setText(student.getFirstName());
        colLastNameLabel.setText(student.getLastName());
        colStudentNumberLabel.setText(student.getStudentNumber());
        colEducationLevelLabel.setText(student.getEducationLevel());
        colAverageGradeLabel.setText(String.valueOf(student.getAverageGrade()));

        // Display formatted date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String formattedDate = student.getBirthDate().format(formatter);
        colBirthDateLabel.setText(formattedDate);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No table initialization needed
    }

    // --- Submit handlers ---
    @FXML
    private void onSubmitFirstName() throws SQLException {
        String newFirstName = inputFirstName.getText().trim();
        if (newFirstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "First name cannot be empty.");
            return;
        }

        StudentDAO.updateFirstName(student.getStudentNumber(), newFirstName);
        student.setFirstName(newFirstName);
        colFirstNameLabel.setText(newFirstName);
    }

    @FXML
    private void onSubmitLastName() throws SQLException {
        String newLastName = inputLastName.getText().trim();
        if (newLastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Last name cannot be empty.");
            return;
        }

        StudentDAO.updateLastName(student.getStudentNumber(), newLastName);
        student.setLastName(newLastName);
        colLastNameLabel.setText(newLastName);
    }

    @FXML
    private void onSubmitBirthDate() throws SQLException {
        LocalDate newDate = inputBirthDate.getValue();
        if (newDate == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a valid birth date.");
            return;
        }

        StudentDAO.updateBirthDate(student.getStudentNumber(), newDate);
        student.setBirthDate(newDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        colBirthDateLabel.setText(newDate.format(formatter));
    }


    @FXML
    private void onSubmitStudentNumber() throws SQLException {
        String newStudentNumber = inputStudentNumber.getText().trim();
        if (!newStudentNumber.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Student number must be exactly 8 digits");
            return;
        }
        StudentDAO.updateStudentNumber(student.getStudentNumber(), newStudentNumber);
        student.setStudentNumber(newStudentNumber);
        colStudentNumberLabel.setText(newStudentNumber);

    }

    @FXML
    private void onSubmitEducationLevel() throws SQLException {
        String newLevel = inputEducationLevel.getText().trim().toUpperCase();
        List<String> validLevels = List.of("B1", "B2", "B3", "M1", "M2");

        if (!validLevels.contains(newLevel)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Education level must be one of: B1, B2, B3, M1, M2.");
            return;
        }

        StudentDAO.updateEducationLevel(student.getStudentNumber(), newLevel);
        student.setEducationLevel(newLevel);
        colEducationLevelLabel.setText(newLevel);
    }

    @FXML
    private void onSubmitAverageGrade() throws SQLException {
        String input = inputAverageGrade.getText().trim();
        try {
            double newGrade = Double.parseDouble(input);
            if (newGrade < 0.0 || newGrade > 20.0) {
                showAlert(Alert.AlertType.ERROR,"Invalid Grade", "Grade must be between 0 and 20.");
                return;
            }

            StudentDAO.updateAverageGrade(student.getStudentNumber(), newGrade);
            student.setAverageGrade(newGrade);
            colAverageGradeLabel.setText(String.valueOf(newGrade));

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Invalid Input",
                    "Please enter a valid number for the average grade.");
        }
    }


    @FXML
    private void onBackButtonClick() {
        // Use any control that exists in the view to get the current stage
        Stage stage = (Stage) inputFirstName.getScene().getWindow();
        stage.close();
    }

    // === Utility Methods ===
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
