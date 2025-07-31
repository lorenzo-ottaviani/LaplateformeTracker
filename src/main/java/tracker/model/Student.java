package tracker.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * Represents a student with observable JavaFX properties.
 * Enables data binding in JavaFX views.
 */
public class Student {

    // --- Attributes (JavaFX Properties) ---
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> birthDate;
    private final StringProperty studentNumber;
    private final StringProperty educationLevel;
    private final DoubleProperty averageGrade;

    // --- Constructor ---
    /**
     * Main constructor for the Student class.
     *
     * @param firstName      The student's first name.
     * @param lastName       The student's last name.
     * @param birthDate      The student's date of birth.
     * @param studentNumber  The unique student number.
     * @param educationLevel The education level (e.g., B1, M2, etc.).
     * @param averageGrade   The student's average grade.
     */
    public Student(String firstName, String lastName, LocalDate birthDate, String studentNumber, String educationLevel,
                   double averageGrade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.studentNumber = new SimpleStringProperty(studentNumber);
        this.educationLevel = new SimpleStringProperty(educationLevel);
        this.averageGrade = new SimpleDoubleProperty(averageGrade);
    }

    // --- Standard Getters ---

    /**
     * Gets the student's first name.
     *
     * @return first name as a String
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Gets the student's last name.
     *
     * @return last name as a String
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Gets the student's birthdate.
     *
     * @return birthdate as a LocalDate
     */
    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    /**
     * Gets the student's number.
     *
     * @return student number as a String
     */
    public String getStudentNumber() {
        return studentNumber.get();
    }

    /**
     * Gets the student's education level.
     *
     * @return education level as a String
     */
    public String getEducationLevel() {
        return educationLevel.get();
    }

    /**
     * Gets the student's average grade.
     *
     * @return average grade as a double
     */
    public double getAverageGrade() {
        return averageGrade.get();
    }

    // --- Standard Setters ---

    /**
     * Sets the student's first name.
     *
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * Sets the student's last name.
     *
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * Sets the student's birthdate.
     *
     * @param birthDate new birthdate
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }

    /**
     * Sets the student's number.
     *
     * @param studentNumber new student number
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber.set(studentNumber);
    }

    /**
     * Sets the student's education level.
     *
     * @param educationLevel new education level
     */
    public void setEducationLevel(String educationLevel) {
        this.educationLevel.set(educationLevel);
    }

    /**
     * Sets the student's average grade.
     *
     * @param averageGrade new average grade
     */
    public void setAverageGrade(double averageGrade) {
        this.averageGrade.set(averageGrade);
    }

    // --- Property Getters for JavaFX Binding ---

    /**
     * Returns the first name property.
     *
     * @return StringProperty for first name
     */
    public StringProperty firstNameProperty() {
        return firstName;
    }

    /**
     * Returns the last name property.
     *
     * @return StringProperty for last name
     */
    public StringProperty lastNameProperty() {
        return lastName;
    }

    /**
     * Returns the birthdate property.
     *
     * @return ObjectProperty for birthdate
     */
    public ObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    /**
     * Returns the student number property.
     *
     * @return StringProperty for student number
     */
    public StringProperty studentNumberProperty() {
        return studentNumber;
    }

    /**
     * Returns the education level property.
     *
     * @return StringProperty for education level
     */
    public StringProperty educationLevelProperty() {
        return educationLevel;
    }

    /**
     * Returns the average grade property.
     *
     * @return DoubleProperty for average grade
     */
    public DoubleProperty averageGradeProperty() {
        return averageGrade;
    }
}
