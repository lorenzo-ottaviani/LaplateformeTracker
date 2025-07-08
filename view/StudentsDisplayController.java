package tracker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tracker.DAO.StudentDAO;
import tracker.model.Student;
import java.sql.Date;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class StudentsDisplayController implements Initializable {

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> colFirstName;

    @FXML
    private TableColumn<Student, String> colLastName;

    @FXML
    private TableColumn<Student, Date> colBirthDate;

    @FXML
    private TableColumn<Student, String> colStudentNumber;

    @FXML
    private TableColumn<Student, String> colEducationLevel;

    @FXML
    private TableColumn<Student, Double> colAverageGrade;

    @FXML
    private Pagination pagination;

    private final static int ROWS_PER_PAGE = 10;
    private ObservableList<Student> allStudents;

    public void setStudentData(List<Student> students) {
        this.allStudents = FXCollections.observableArrayList(students);

        // Recalculer la pagination si les données sont mises à jour après initialize()
        int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(this::createPage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mock data

        ArrayList<Student> dbStudents = StudentDAO.selectAll();
        List<Student> studentList = new ArrayList<>();
        for (Student s : dbStudents) {
            studentList.add(new Student(
                    s.firstNameProperty().get(),
                    s.lastNameProperty().get(),
                    s.birthDateProperty().get(),
                    s.studentNumberProperty().get(),
                    s.educationLevelProperty().get(),
                    s.averageGradeProperty().get()
            ));
        }
        allStudents = FXCollections.observableArrayList(studentList);

        // Initialize table columns
        colFirstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        colBirthDate.setCellValueFactory(cell -> cell.getValue().birthDateProperty());
        colStudentNumber.setCellValueFactory(cell -> cell.getValue().studentNumberProperty());
        colEducationLevel.setCellValueFactory(cell -> cell.getValue().educationLevelProperty());
        colAverageGrade.setCellValueFactory(cell -> cell.getValue().averageGradeProperty().asObject());

        // Configure pagination
        int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Student> createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allStudents.size());
        studentTable.setItems(FXCollections.observableArrayList(allStudents.subList(fromIndex, toIndex)));
        return studentTable;
    }

    @FXML
    protected void onLogoutButtonClick() {
        try {
            // Load the FXML file for the Register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Get the current stage from a control (e.g., userField)
            Stage stage = (Stage) studentTable.getScene().getWindow();

            // Create a new scene with the Register view
            Scene scene = new Scene(loginRoot, 800, 600);

            // Replace the current scene with the new one
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
