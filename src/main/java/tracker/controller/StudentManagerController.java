package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tracker.DAO.StudentDAO;
import tracker.model.Student;

import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentManagerController implements Initializable {

    // Labels (to display current data above editable fields)
    @FXML private Label colFirstNameLabel;
    @FXML private Label colLastNameLabel;
    @FXML private Label colBirthDateLabel;
    @FXML private Label colStudentNumberLabel;
    @FXML private Label colEducationLevelLabel;
    @FXML private Label colAverageGradeLabel;

    // Editable input fields
    @FXML private TextField inputFirstName;
    @FXML private TextField inputLastName;
    @FXML private DatePicker inputBirthDate;
    @FXML private TextField inputStudentNumber;
    @FXML private TextField inputEducationLevel;
    @FXML private TextField inputAverageGrade;

    private Student student;

    public void setStudentData(Student student) {
        this.student = student;

        // Display current values in labels
        colFirstNameLabel.setText(student.firstNameProperty().get());
        colLastNameLabel.setText(student.lastNameProperty().get());
        colStudentNumberLabel.setText(student.studentNumberProperty().get());
        colEducationLevelLabel.setText(student.educationLevelProperty().get());
        colAverageGradeLabel.setText(String.valueOf(student.averageGradeProperty().get()));

        // Display formated date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String formattedDate = student.birthDateProperty().get().format(formatter);
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
        if (!newFirstName.isEmpty()) {
            StudentDAO.updateFirstNameStudent(student.studentNumberProperty().get(), newFirstName);
            student.firstNameProperty().set(newFirstName);
            colFirstNameLabel.setText(newFirstName);
        }
    }

    @FXML
    private void onSubmitLastName() throws  SQLException {
        String newLastName = inputLastName.getText().trim();
        if (!newLastName.isEmpty()) {
            StudentDAO.updateLastNameStudent(student.studentNumberProperty().get(), newLastName);
            student.lastNameProperty().set(newLastName);
            colLastNameLabel.setText(newLastName);
        }
    }

    @FXML
    private void onSubmitBirthDate() {
        LocalDate newDate = inputBirthDate.getValue();
        if (newDate != null) {
            student.birthDateProperty().set(newDate);
            colBirthDateLabel.setText(newDate.toString());
        }
    }

    @FXML
    private void onSubmitStudentNumber() {
        String newValue = inputStudentNumber.getText().trim();
        if (!newValue.isEmpty()) {
            student.studentNumberProperty().set(newValue);
            colStudentNumberLabel.setText(newValue);
        }
    }

    @FXML
    private void onSubmitEducationLevel() {
        String newValue = inputEducationLevel.getText().trim();
        if (!newValue.isEmpty()) {
            student.educationLevelProperty().set(newValue);
            colEducationLevelLabel.setText(newValue);
        }
    }

    @FXML
    private void onSubmitAverageGrade() {
        try {
            double newValue = Double.parseDouble(inputAverageGrade.getText().trim());
            student.averageGradeProperty().set(newValue);
            colAverageGradeLabel.setText(String.valueOf(newValue));
        } catch (NumberFormatException e) {
            // Optionally show validation error to user
        }
    }

    @FXML
    private void onBackButtonClick() {
        // Use any control that exists in the view to get the current stage
        Stage stage = (Stage) inputFirstName.getScene().getWindow();
        stage.close();
    }
}
