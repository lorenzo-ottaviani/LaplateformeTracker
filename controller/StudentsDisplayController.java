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

import tracker.DAO.StudentDAO;
import tracker.model.Student;

import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for displaying and managing students with pagination, edit and delete functionalities.
 */
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
    private TableColumn<Student, Void> colEdit;

    @FXML
    private TableColumn<Student, Void> colDelete;

    @FXML
    private Pagination pagination;

    private static final int ROWS_PER_PAGE = 10;
    private ObservableList<Student> allStudents;

    /**
     * Initializes the controller after the FXML fields are injected.
     * Loads students from the database, initializes the table and pagination.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if unknown.
     * @param resourceBundle The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Student> dbStudents = StudentDAO.selectAll();
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set row height to 33 pixels
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setPrefHeight(33);
            return row;
        });

        // Initialize table columns with property bindings
        colFirstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
        colBirthDate.setCellValueFactory(cell -> cell.getValue().birthDateProperty());
        colStudentNumber.setCellValueFactory(cell -> cell.getValue().studentNumberProperty());
        colEducationLevel.setCellValueFactory(cell -> cell.getValue().educationLevelProperty());
        colAverageGrade.setCellValueFactory(cell -> cell.getValue().averageGradeProperty().asObject());

        // Add buttons columns
        addEditButtonToTable();
        addDeleteButtonToTable();

        // Setup pagination
        int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(this::createPage);
    }

    /**
     * Creates a page of the table based on the page index.
     *
     * @param pageIndex the index of the page to create
     * @return a Node required by Pagination, here unused and returns empty Label
     */
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, allStudents.size());
        studentTable.setItems(FXCollections.observableArrayList(allStudents.subList(fromIndex, toIndex)));
        studentTable.refresh(); // force update of cells, fixes rendering bugs with buttons
        return new Label(""); // Required by Pagination but not displayed
    }


    /**
     * Adds an Edit button to each row of the table.
     * Clicking it opens the Student Manager view to edit student details.
     */
    private void addEditButtonToTable() {
        colEdit.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-background-radius: 8;");
                editButton.setOnAction(event -> {
                    Student selectedStudent = getTableView().getItems().get(getIndex());
                    openStudentManagerView(selectedStudent);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    /**
     * Adds a Delete button to each row of the table.
     * Clicking it opens a confirmation dialog before deleting the student.
     */
    private void addDeleteButtonToTable() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-background-radius: 8;");
                deleteButton.setOnAction(event -> {
                    Student selectedStudent = getTableView().getItems().get(getIndex());
                    openDeleteConfirmation(selectedStudent);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    /**
     * Opens the Student Manager view to edit the selected student.
     *
     * @param student the student to edit
     */
    private void openStudentManagerView(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/student-manager-view.fxml"));
            Parent root = loader.load();

            StudentManagerController controller = loader.getController();
            controller.setStudentData(student);

            Stage stage = new Stage();
            stage.setTitle("Manage Student");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Delete Confirmation dialog for the selected student.
     *
     * @param student the student to confirm deletion for
     */
    private void openDeleteConfirmation(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/delete-confirmation-view.fxml"));
            Parent root = loader.load();

            DeleteConfirmController controller = loader.getController();
            controller.setStudent(student, this);

            Stage stage = new Stage();
            stage.setTitle("Delete Confirmation");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Logout button click event.
     * Loads the login view and replaces the current scene.
     */
    @FXML
    protected void onLogoutButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Stage stage = (Stage) studentTable.getScene().getWindow();

            Scene scene = new Scene(loginRoot, 800, 600);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the student table data from the database and updates pagination.
     */
    public void refreshStudentTable() {
        try {
            List<Student> updatedList = StudentDAO.selectAll();
            allStudents.setAll(updatedList);

            int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
            pagination.setPageCount(Math.max(pageCount, 1));
            pagination.setPageFactory(this::createPage);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
