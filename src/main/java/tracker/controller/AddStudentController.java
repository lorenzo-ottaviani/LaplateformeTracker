package tracker.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import tracker.DAO.StudentDAO;
import tracker.model.Student;

/**
 * Controller responsible for adding a new student.
 * Validates inputs, inserts the student into the database,
 * and manages navigation back to the student display view.
 */
public class AddStudentController {

    // === FXML UI components ===
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDateField;
    @FXML private TextField studentNumberField;
    @FXML private TextField educationLevelField;
    @FXML private TextField averageGradeField;
    @FXML private Label addErrorLabel;

    // === Event Handlers ===

    /**
     * Called when the "Add Student" button is clicked.
     * Validates inputs, inserts the student into DB, shows feedback.
     */
    @FXML
    protected void onAddStudentButtonClick() {
        addErrorLabel.setVisible(false);

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        LocalDate birthDate = birthDateField.getValue();
        String studentNumber = studentNumberField.getText().trim();
        String educationLevel = educationLevelField.getText().trim().toUpperCase();
        String averageGradeText = averageGradeField.getText().trim();

        String validationError = validateInputs(firstName, lastName, birthDate, studentNumber, educationLevel,
                averageGradeText);
        if (validationError != null) {
            showError(validationError);
            return;
        }

        try {
            double averageGrade = Double.parseDouble(averageGradeText);
            Student newStudent = new Student(firstName, lastName, birthDate, studentNumber, educationLevel,
                    averageGrade);
            StudentDAO.insertStudent(newStudent);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
            clearForm();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "An error occurred while adding the new student. Please try again.");
        }
    }

    /**
     * Called when the "Back" button is clicked.
     * Navigates back to the students display view.
     */
    @FXML
    protected void onBackButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/students-display-view.fxml"));
            Parent studentsDisplayRoot = loader.load();

            Stage stage = (Stage) firstNameField.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(studentsDisplayRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the student dashboard.");
        }
    }

    // === Validation Methods ===

    /**
     * Validates all input fields.
     *
     * @return null if valid, else the first error message found.
     */
    private String validateInputs(String firstName, String lastName, LocalDate birthDate,
                                  String studentNumber, String educationLevel, String averageGradeText) {

        if (firstName.isEmpty()) return "First name cannot be empty.";
        if (lastName.isEmpty()) return "Last name cannot be empty.";
        if (birthDate == null) return "Please select a valid birth date.";
        if (!studentNumber.matches("\\d{8}")) return "Student number must be exactly 8 digits.";

        List<String> validLevels = List.of("B1", "B2", "B3", "M1", "M2");
        if (!validLevels.contains(educationLevel)) {
            return "Education level must be one of: B1, B2, B3, M1, M2.";
        }

        try {
            double grade = Double.parseDouble(averageGradeText);
            if (grade < 0.0 || grade > 20.0) return "Grade must be between 0 and 20.";
        } catch (NumberFormatException e) {
            return "Average grade must be a number.";
        }

        return null;
    }

    // === UI Feedback Methods ===

    /**
     * Shows an error message in the label below the form.
     */
    private void showError(String message) {
        addErrorLabel.setText(message);
        addErrorLabel.setStyle("-fx-text-fill: #e63946; -fx-font-weight: bold;");
        addErrorLabel.setVisible(true);
    }

    /**
     * Shows a JavaFX alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clears the form inputs and hides the error label.
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        birthDateField.setValue(null);
        studentNumberField.clear();
        educationLevelField.clear();
        averageGradeField.clear();
        addErrorLabel.setVisible(false);
    }
}
