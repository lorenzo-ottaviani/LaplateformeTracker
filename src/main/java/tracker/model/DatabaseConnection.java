package tracker.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Manages the connection to PostgreSQL database.
 * Checks if the target database exists, creates it if necessary,
 * and returns a connection to that database.
 */
public class DatabaseConnection {

    private static String DB_HOST;
    private static int DB_PORT;
    private static String DB_USER;
    private static String DB_PASS;
    private static String DB_NAME;

    static {
        loadConfig();
    }

    /**
     * Loads the database configuration from application.properties file.
     */
    private static void loadConfig() {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/tracker/application.properties")) {
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

        } catch (IOException e) {
            System.err.println("❌ Failed to load database configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid port number in configuration file: " + e.getMessage());
        }
    }

    /**
     * Connects to the PostgreSQL server default database (usually "postgres"),
     * checks if the target database exists, creates it if it does not exist,
     * then returns a connection to the target database.
     *
     * @return Connection to the target database, or null if connection failed.
     */
    public static Connection connect() {
        String defaultUrl = String.format("jdbc:postgresql://%s:%d/postgres", DB_HOST, DB_PORT);

        try (Connection conn = DriverManager.getConnection(defaultUrl, DB_USER, DB_PASS)) {
            System.out.println("✅ Connected to PostgreSQL server (default database).");

            if (!databaseExists(conn, DB_NAME)) {
                System.out.println("⚠️ Database '" + DB_NAME + "' does not exist. Creating...");
                DatabaseCreator.createDatabase(conn, DB_NAME, DB_HOST, DB_PORT, DB_USER, DB_PASS);
                System.out.println("✅ Database '" + DB_NAME + "' created.");
            } else {
                System.out.println("ℹ️ Database '" + DB_NAME + "' already exists.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error connecting to PostgreSQL server or checking DB existence: " + e.getMessage());
            return null;
        }

        // Connect to the target database
        String targetUrl = String.format("jdbc:postgresql://%s:%d/%s", DB_HOST, DB_PORT, DB_NAME);

        try {
            Connection connToDb = DriverManager.getConnection(targetUrl, DB_USER, DB_PASS);
            System.out.println("✅ Successfully connected to PostgreSQL database '" + DB_NAME + "'!");
            return connToDb;
        } catch (SQLException e) {
            System.err.println("❌ Could not connect to database '" + DB_NAME + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a valid Connection to the target database.
     * Throws RuntimeException if connection fails.
     *
     * @return a valid Connection object.
     * @throws RuntimeException if connection could not be established.
     */
    public static Connection getConnection() {
        Connection conn = connect();
        if (conn == null) {
            throw new RuntimeException("Failed to establish database connection.");
        }
        return conn;
    }

    /**
     * Checks whether a database with the specified name exists.
     *
     * @param conn   Connection to the default database.
     * @param dbName Name of the database to check.
     * @return true if the database exists, false otherwise.
     * @throws SQLException if a database access error occurs.
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
