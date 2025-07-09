package tracker.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Handles configuration and PostgreSQL connection.
 * Ensures the database exists at startup, and provides connections afterwards.
 */
public class DatabaseConnection {

    private static String DB_HOST;
    private static int DB_PORT;
    private static String DB_USER;
    private static String DB_PASS;
    private static String DB_NAME;

    /**
     * Loads DB configuration from properties file.
     * @throws IOException if config file is missing or invalid.
     */
    public static void loadConfig() throws IOException {
        try (FileInputStream input = new FileInputStream("application.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                throw new IOException("❌ Could not find tracker/application.properties in classpath.");
            }

            prop.load(input);

            DB_HOST = prop.getProperty("db.host");
            DB_PORT = Integer.parseInt(prop.getProperty("db.port"));
            DB_USER = prop.getProperty("db.user");
            DB_PASS = prop.getProperty("db.password");
            DB_NAME = prop.getProperty("db.name");

        } catch (NumberFormatException e) {
            throw new IOException("❌ Invalid port number in configuration file: " + e.getMessage());
        }
    }

    /**
     * Initializes the database: loads config and ensures DB exists.
     * @throws IOException if configuration fails
     * @throws SQLException if DB server or creation fails
     */
    public static void initialize() throws IOException, SQLException {
        loadConfig();
        checkAndCreateDatabaseIfNeeded();
    }

    /**
     * Returns a connection to the target database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        String targetUrl = String.format("jdbc:postgresql://%s:%d/%s", DB_HOST, DB_PORT, DB_NAME);
        return DriverManager.getConnection(targetUrl, DB_USER, DB_PASS);
    }

    /**
     * Connects to the default PostgreSQL database ("postgres") and checks whether
     * the target application database exists. If it does not exist, the method
     * delegates the creation to {@link DatabaseCreator#createDatabase}.
     *
     * @throws SQLException if the connection to the server fails or if any error occurs
     *                      during the database existence check or creation process.
     */
    private static void checkAndCreateDatabaseIfNeeded() throws SQLException {
        String defaultUrl = String.format("jdbc:postgresql://%s:%d/postgres", DB_HOST, DB_PORT);

        try (Connection conn = DriverManager.getConnection(defaultUrl, DB_USER, DB_PASS)) {
            if (!databaseExists(conn, DB_NAME)) {
                System.out.println("⚠️ Database '" + DB_NAME + "' does not exist. Creating...");
                DatabaseCreator.createDatabase(conn, DB_NAME, DB_HOST, DB_PORT, DB_USER, DB_PASS);
                System.out.println("✅ Database '" + DB_NAME + "' created.");
            } else {
                System.out.println("ℹ️ Database '" + DB_NAME + "' already exists.");
            }
        }
    }

    /**
     * Checks whether a database with the given name exists on the PostgreSQL server.
     *
     * @param conn   An active connection to the PostgreSQL server (typically the "postgres" default database).
     * @param dbName The name of the database to check.
     * @return {@code true} if the database exists, {@code false} otherwise.
     * @throws SQLException if a database access error occurs during the query.
     */
    private static boolean databaseExists(Connection conn, String dbName) throws SQLException {
        String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkDbQuery)) {
            stmt.setString(1, dbName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
