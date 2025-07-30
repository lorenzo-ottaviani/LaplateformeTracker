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
import tracker.DAO.StudentDAO;
import tracker.model.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for editing an existing student.
 * Fields are pre-filled with current student data.
 * After editing, updates the student in the database.
 */
public class EditStudentController {

    // === FXML UI Components ===
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDateField;
    @FXML private TextField studentNumberField;
    @FXML private TextField educationLevelField;
    @FXML private TextField averageGradeField;
    @FXML private Label editErrorLabel;

    // === Data Fields ===
    private Student studentToEdit;

    // === Data Injection ===

    /**
     * Injects the student to be edited and pre-fills the form fields.
     * Called externally by the view loader or controller.
     *
     * @param student the student object to edit
     */
    public void setStudentToEdit(Student student) {
        this.studentToEdit = student;

        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        birthDateField.setValue(student.getBirthDate());
        studentNumberField.setText(student.getStudentNumber());
        educationLevelField.setText(student.getEducationLevel());
        averageGradeField.setText(String.valueOf(student.getAverageGrade()));

        editErrorLabel.setVisible(false);
    }

    // === Event Handlers ===

    /**
     * Handles the "Save Changes" button click.
     * Validates the inputs and updates the student in the database.
     */
    @FXML
    protected void onSaveButtonClick() {
        editErrorLabel.setVisible(false);

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
            String originalStudentNumber = studentToEdit.getStudentNumber();

            Student updatedStudent = new Student(firstName, lastName, birthDate, studentNumber, educationLevel,
                    averageGrade);

            StudentDAO.updateStudent(originalStudentNumber, updatedStudent);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "An error occurred while updating the student.");
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
     * Validates all form input fields.
     *
     * @return null if all inputs are valid; otherwise, returns an error message
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
            return "Average grade must be a numeric value.";
        }

        return null;
    }

    // === UI Feedback Methods ===

    /**
     * Displays an error message label under the form.
     *
     * @param message the error message to show
     */
    private void showError(String message) {
        editErrorLabel.setText(message);
        editErrorLabel.setStyle("-fx-text-fill: #e63946; -fx-font-weight: bold;");
        editErrorLabel.setVisible(true);
    }

    /**
     * Displays a JavaFX alert dialog.
     *
     * @param type    the type of alert (e.g. INFORMATION, ERROR)
     * @param title   the dialog title
     * @param message the message content
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
