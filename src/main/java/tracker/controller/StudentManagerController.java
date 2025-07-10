package tracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tracker.model.Student;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentManagerController implements Initializable {

    @FXML private TextField inputFirstName;
    @FXML private TextField inputLastName;
    @FXML private DatePicker inputBirthDate;
    @FXML private TextField inputStudentNumber;
    @FXML private TextField inputEducationLevel;
    @FXML private TextField inputAverageGrade;

    private Student student;

    public void setStudentData(Student student) {
        this.student = student;

        // Pré-remplissage des champs
        inputFirstName.setText(student.firstNameProperty().get());
        inputLastName.setText(student.lastNameProperty().get());
        inputBirthDate.setValue(student.birthDateProperty().get());
        inputStudentNumber.setText(student.studentNumberProperty().get());
        inputEducationLevel.setText(student.educationLevelProperty().get());
        inputAverageGrade.setText(String.valueOf(student.averageGradeProperty().get()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // rien ici
    }

    @FXML
    private void onSaveAllClick() {
        student.firstNameProperty().set(inputFirstName.getText().trim());
        student.lastNameProperty().set(inputLastName.getText().trim());
        student.birthDateProperty().set(inputBirthDate.getValue());
        student.studentNumberProperty().set(inputStudentNumber.getText().trim());
        student.educationLevelProperty().set(inputEducationLevel.getText().trim());

        try {
            double average = Double.parseDouble(inputAverageGrade.getText().trim());
            student.averageGradeProperty().set(average);
        } catch (NumberFormatException e) {
            System.err.println("⛔ Invalid average grade input.");
        }
    }

    @FXML
    private void onBackButtonClick() {
        Stage stage = (Stage) inputFirstName.getScene().getWindow();
        stage.close();
    }
}
