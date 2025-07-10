package tracker.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Student {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> birthDate;
    private final StringProperty studentNumber;
    private final StringProperty educationLevel;
    private final DoubleProperty averageGrade;

    public Student(String firstName, String lastName, LocalDate birthDate, String studentNumber, String educationLevel,
                   double averageGrade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.studentNumber = new SimpleStringProperty(studentNumber);
        this.educationLevel = new SimpleStringProperty(educationLevel);
        this.averageGrade = new SimpleDoubleProperty(averageGrade);
    }

    public String getStudentNumber() {
        return studentNumber.get();
    }


    // Getters for properties
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public StringProperty studentNumberProperty() { return studentNumber; }
    public StringProperty educationLevelProperty() { return educationLevel; }
    public DoubleProperty averageGradeProperty() { return averageGrade; }
}
