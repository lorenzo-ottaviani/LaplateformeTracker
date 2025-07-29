package tracker.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a User in the system with properties for ID, email, and password.
 * Uses JavaFX properties for easy binding in UI components.
 */
public class User {
    private final IntegerProperty userId;
    private final StringProperty userMail;
    private final StringProperty userPassword;

    /**
     * Constructs a User with the specified email and password.
     * The userId is initialized to 0 by default.
     *
     * @param userMail     the user's email address
     * @param userPassword the user's password
     */
    public User(String userMail, String userPassword) {
        this.userMail = new SimpleStringProperty(userMail);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.userId = new SimpleIntegerProperty(0);
    }

    /**
     * Returns the property for the user ID.
     *
     * @return the userId property
     */
    public IntegerProperty userIdProperty() {
        return userId;
    }

    /**
     * Returns the property for the user's email.
     *
     * @return the userMail property
     */
    public StringProperty userMailProperty() {
        return userMail;
    }

    /**
     * Returns the property for the user's password.
     *
     * @return the userPassword property
     */
    public StringProperty userPasswordProperty() {
        return userPassword;
    }
}

