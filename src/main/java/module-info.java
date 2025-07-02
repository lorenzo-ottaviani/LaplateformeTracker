module plateformetracker.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens tracker.view to javafx.fxml;
    exports tracker.view;
}