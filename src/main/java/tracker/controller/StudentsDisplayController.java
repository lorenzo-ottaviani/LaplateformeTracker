package tracker.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import tracker.DAO.StudentDAO;
import tracker.model.Student;


/**
 * Controller for displaying and managing students with pagination, edit and delete functionalities.
 */
public class StudentsDisplayController implements Initializable {

    // === Constants ===
    private static final int ROWS_PER_PAGE = 10;

    // === Internal data ===
    private ObservableList<Student> allStudents;

    // === FXML UI components ===
    @FXML private TableView<Student> studentTable;
    @FXML private Pagination pagination;
    @FXML private TextField searchIdField;

    // === FXML Table columns ===
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, LocalDate> colBirthDate;
    @FXML private TableColumn<Student, String> colStudentNumber;
    @FXML private TableColumn<Student, String> colEducationLevel;
    @FXML private TableColumn<Student, Double> colAverageGrade;
    @FXML private TableColumn<Student, Void> colEdit;
    @FXML private TableColumn<Student, Void> colDelete;

    // === Initialization ===
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
                        s.getFirstName(),
                        s.getLastName(),
                        s.getBirthDate(),
                        s.getStudentNumber(),
                        s.getEducationLevel(),
                        s.getAverageGrade()
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

        // Format and display the birthdate as d/M/yyyy (e.g., 5/12/2000)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        colBirthDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Add buttons columns
        addEditButtonToTable();
        addDeleteButtonToTable();

        // Setup pagination
        int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(this::createPage);
    }

    // === Pagination handling ===
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
        studentTable.refresh();
        return new Label(""); // Required by Pagination but not displayed
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

    // === Table button setup ===
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
                    openDeleteConfirmationView(selectedStudent);
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

    // === View opening methods ===
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
     * Opens the Delete Confirmation view for the selected student.
     *
     * @param student the student to confirm deletion for
     */
    private void openDeleteConfirmationView(Student student) {
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Unable to open the delete confirmation dialog.");
        }
    }

    // === UI Event handlers ===
    /**
     * Triggered when the Add button is clicked.
     * Opens a new window for adding a new student entry via the Student Manager view.
     * The form will be empty to allow creation of a fresh student record.
     */
    @FXML
    protected void onAddButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/add-student-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Stage stage = (Stage) studentTable.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(loginRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the add student screen.");
        }
    }

    /**
     * Triggered when the Search button is clicked.
     * Searches for a student by the entered student number.
     * If found, opens the Student Manager view with the student's data.
     * If not found or on error, displays an appropriate alert to the user.
     */
    @FXML
    protected void onSearchButtonClick() {
        String studentNumber = searchIdField.getText().trim();

        if (studentNumber.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Failed", "Please enter a student number.");
            return;
        }

        try {
            Student student = StudentDAO.findByStudentNumber(studentNumber);

            if (student != null) {
                openStudentManagerView(student);
            } else {
                showAlert(Alert.AlertType.WARNING, "Search Failed",
                        "No student found with the given student number.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "An error occurred while searching for the student.");
        }
    }


    /**
     * Handles the Logout button click event.
     * Switches the current scene to the login view.
     */
    @FXML
    protected void onLogoutButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tracker/view/login-view.fxml"));
            Parent loginRoot = fxmlLoader.load();

            Stage stage = (Stage) studentTable.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(loginRoot);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the login screen.");
        }
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
