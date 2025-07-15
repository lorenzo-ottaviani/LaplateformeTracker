package tracker.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import tracker.model.DatabaseConnection;

public class  TrackerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TrackerApp.class.getResource("/tracker/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(TrackerApp.class.getResource("/tracker/style/style.css").toExternalForm());

        stage.setTitle("TRACK YOU !");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            DatabaseConnection.initialize();
            System.out.println("✅ Database is ready.");
            launch();
        } catch (Exception e) {
            System.err.println("❌ Critical error during DB initialization: " + e.getMessage());
            System.exit(1); // Force exit: can't run app without DB
        }
    }
}
