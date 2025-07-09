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
import java.sql.Date;
import java.sql.SQLException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    private TableColumn<Student, LocalDate> colBirthDate;

    @FXML
    private TableColumn<Student, String> colStudentNumber;

    @FXML
    private TableColumn<Student, String> colEducationLevel;

    @FXML
    private TableColumn<Student, Double> colAverageGrade;

    @FXML
    private TableColumn<Student, Void> colSelect;

    @FXML
    private TableColumn<Student, Void> colDelete;

    @FXML
    private Pagination pagination;

    private final static int ROWS_PER_PAGE = 10;
    private ObservableList<Student> allStudents;

    public void setStudentData(List<Student> students) {
        this.allStudents = FXCollections.observableArrayList(students);

        int pageCount = (int) Math.ceil((double) allStudents.size() / ROWS_PER_PAGE);
        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setPageFactory(this::createPage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mock data
        ArrayList<Student> dbStudents = null;
        try {
            dbStudents = StudentDAO.selectAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        addSelectButtonToTable();
        addDeleteButtonToTable();

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

    private void addSelectButtonToTable() {
        colSelect.setCellFactory(param -> new TableCell<>() {
            private final Button selectButton = new Button("Select");

            {
                selectButton.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-background-radius: 8;");
                selectButton.setOnAction(event -> {
                    Student selectedStudent = getTableView().getItems().get(getIndex());
                    openStudentManagerView(selectedStudent);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(selectButton);
                }
            }
        });
    }

    private void addDeleteButtonToTable() {
        colDelete.setCellFactory(param -> {
            StudentsDisplayController controllerRef = this;  // 🔥 capture correcte du bon "this"

            return new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-background-radius: 8;");
                    deleteButton.setOnAction(event -> {
                        Student selectedStudent = getTableView().getItems().get(getIndex());
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/delete-confirmation-view.fxml"));
                            Parent root = loader.load();

                            DeleteConfirmController controller = loader.getController();
                            controller.setStudent(selectedStudent, controllerRef); // ✅ là c'est bon

                            Stage stage = new Stage();
                            stage.setTitle("Confirmation suppression");
                            stage.setScene(new Scene(root, 600, 400));
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });
    }


    private void openStudentManagerView(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/student-manager-view.fxml"));
            Parent root = loader.load();

            // Passer les données de l'étudiant au nouveau contrôleur
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

    private void openDeleteConfirmation(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tracker/view/delete-confirmation-view.fxml"));
            Parent root = loader.load();

            // Passer les données de l'étudiant au nouveau contrôleur
            StudentManagerController controller = loader.getController();
            controller.setStudentData(student);

            Stage stage = new Stage();
            stage.setTitle("CONFIRM DELETE");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
