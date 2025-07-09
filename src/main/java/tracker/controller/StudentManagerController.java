package tracker.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tracker.model.Student;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentManagerController implements Initializable {

    @FXML
    private TableView<Student> studentDetailTable;

    @FXML
    private TableColumn<Student, String> colFirstName;

    @FXML
    private TableColumn<Student, String> colLastName;

    @FXML
    private TableColumn<Student, LocalDate> colBirthDate;

    @FXML
    private TableColumn<Student, String> colStudentNumber;

    @FXML
    private TableColumn<Student, String> colEducationLevel;

    @FXML
    private TableColumn<Student, Double> colAverageGrade;

    private Student student;

    public void setStudentData(Student student) {
        this.student = student;

        // Ajout d'une seule ligne dans la table
        studentDetailTable.setItems(FXCollections.observableArrayList(student));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colFirstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        colBirthDate.setCellValueFactory(cell -> cell.getValue().birthDateProperty());
        colStudentNumber.setCellValueFactory(cell -> cell.getValue().
                studentNumberProperty());
        colEducationLevel.setCellValueFactory(cell -> cell.getValue().
                educationLevelProperty());
        colAverageGrade.setCellValueFactory(cell -> cell.getValue().
                averageGradeProperty().asObject());
    }
}
