package tracker.model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
   private final IntegerProperty userId;
    private final StringProperty userMail;
    private  final StringProperty userPassword;

    public User(String userMail, String userPassword){
        this.userMail = new SimpleStringProperty(userMail);
        this.userPassword = new SimpleStringProperty(userPassword);
        this.userId = new SimpleIntegerProperty(0);
    }

    public IntegerProperty userIdProperty()  {return userId;}
    public StringProperty userMailProperty() { return  userMail;}
    public StringProperty userPasswordProperty (){  return userPassword; }

}
