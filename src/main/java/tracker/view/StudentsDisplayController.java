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
import tracker.model.Student;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StudentsDisplayController implements Initializable {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, LocalDate> colBirthDate;
    @FXML private TableColumn<Student, String> colStudentNumber;
    @FXML private TableColumn<Student, String> colEducationLevel;
    @FXML private TableColumn<Student, Double> colAverageGrade;
    @FXML private Pagination pagination;

    private final static int ROWS_PER_PAGE = 10;
    private ObservableList<Student> allStudents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mock data
        allStudents = FXCollections.observableArrayList(
                new Student("Alice", "Martin", LocalDate.of(2001, 3, 15),
                        "S001", "Licence", 14.2),
                new Student("Bob", "Durand", LocalDate.of(2000, 11, 5),
                        "S002", "Master", 13.5),
                new Student("Chloé", "Petit", LocalDate.of(2002, 6, 22),
                        "S003", "Licence", 15.8)
        );

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage stage = (Stage) studentTable.getScene().getWindow();
            stage.setScene(new Scene(loginRoot, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
