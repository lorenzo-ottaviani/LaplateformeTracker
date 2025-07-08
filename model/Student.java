package tracker.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

public class Student {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<Date> birthDate;
    private final StringProperty studentNumber;
    private final StringProperty educationLevel;
    private final DoubleProperty averageGrade;

    public Student(String firstName, String lastName, Date birthDate, String studentNumber, String educationLevel,
                   double averageGrade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDate = new SimpleObjectProperty<Date>(birthDate);
        this.studentNumber = new SimpleStringProperty(studentNumber);
        this.educationLevel = new SimpleStringProperty(educationLevel);
        this.averageGrade = new SimpleDoubleProperty(averageGrade);
    }

    // Getters for properties
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public ObjectProperty<Date> birthDateProperty() { return birthDate; }
    public StringProperty studentNumberProperty() { return studentNumber; }
    public StringProperty educationLevelProperty() { return educationLevel; }
    public DoubleProperty averageGradeProperty() { return averageGrade; }
}

