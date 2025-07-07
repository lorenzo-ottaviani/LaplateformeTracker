package tracker.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import tracker.model.DatabaseConnection;

public class TrackerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TrackerApp.class.getResource("/tracker/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("TRACK YOU !");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✅ Database connection established successfully.");

            launch();  // Start JavaFX application

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to the database: " + e.getMessage());
            System.exit(1);  // Exit app if DB connection fails
        }
    }
}
