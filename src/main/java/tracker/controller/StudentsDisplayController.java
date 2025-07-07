package tracker.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    @FXML
    private TableView<Student> studentTable;

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

    @FXML
    private Pagination pagination;

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
                        "S003", "Licence", 15.8),
                new Student("David", "Lemoine", LocalDate.of(2001, 7, 19),
                        "S004", "Licence", 12.3),
                new Student("Emma", "Bernard", LocalDate.of(2002, 4, 10),
                        "S005", "Master", 16.1),
                new Student("François", "Dubois", LocalDate.of(2000, 9, 23),
                        "S006", "Licence", 11.7),
                new Student("Gabrielle", "Moreau", LocalDate.of(2001, 12, 1),
                        "S007", "Master", 14.9),
                new Student("Hugo", "Garcia", LocalDate.of(2002, 2, 17),
                        "S008", "Licence", 13.3),
                new Student("Isabelle", "Leroy", LocalDate.of(2000, 5, 28),
                        "S009", "Master", 12.8),
                new Student("Julien", "Roux", LocalDate.of(2001, 8, 6),
                        "S010", "Licence", 13.6),
                new Student("Karine", "Simon", LocalDate.of(2002, 1, 15),
                        "S011", "Master", 15.2),
                new Student("Louis", "Michel", LocalDate.of(2000, 3, 30),
                        "S012", "Licence", 14.4),
                new Student("Manon", "Fournier", LocalDate.of(2001, 10, 8),
                        "S013", "Master", 13.9),
                new Student("Nicolas", "Guerin", LocalDate.of(2002, 7, 3),
                        "S014", "Licence", 12.5),
                new Student("Océane", "Martinez", LocalDate.of(2000, 6, 20),
                        "S015", "Master", 15.7)
        );

        // Set row height to 40 pixels
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setPrefHeight(33);
            return row;
        });

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

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allStudents.size());
        studentTable.setItems(FXCollections.observableArrayList(allStudents.subList(fromIndex, toIndex)));
        return new Label(""); // Required by Pagination, but not used for display
    }


    @FXML
    protected void onLogoutButtonClick() {
        try {
            // Load the FXML file for the Register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/login-view.fxml"));
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
