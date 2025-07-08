module tracker.controller {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires java.sql;
    requires spring.security.crypto;

    opens tracker.controller to javafx.fxml;
    exports tracker.controller;
}