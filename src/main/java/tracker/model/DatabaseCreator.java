package tracker.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles creation of PostgreSQL databases and necessary tables.
 */
public class DatabaseCreator {

    /**
     * Creates a new PostgreSQL database with the given name.
     * Then connects to the new database and creates the required tables and types.
     *
     * @param conn   Connection to the default database (e.g., "postgres").
     * @param dbName Name of the database to create.
     * @param host   Database host.
     * @param port   Database port.
     * @param user   Database user.
     * @param pass   Database password.
     * @throws SQLException if an SQL error occurs during database creation or table setup.
     */
    public static void createDatabase(Connection conn, String dbName, String host, int port, String user, String pass)
            throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
        }

        // Connect to the newly created database to create tables and types
        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, dbName);
        try (Connection newDbConn = DriverManager.getConnection(url, user, pass);
             Statement stmt = newDbConn.createStatement()) {

            // Create ENUM type level
            stmt.executeUpdate("DO $$ BEGIN " +
                    "IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'level') THEN " +
                    "CREATE TYPE level AS ENUM('B1', 'B2', 'B3', 'M1', 'M2'); " +
                    "END IF; " +
                    "END$$;");

            // Create trackuser table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS trackuser (" +
                            "usr_id SERIAL PRIMARY KEY, " +
                            "usr_mail VARCHAR(50) NOT NULL, " +
                            "usr_password VARCHAR(100) NOT NULL" +
                            ")");

            // Create trackstudent table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS trackstudent (" +
                            "stud_id SERIAL PRIMARY KEY, " +
                            "stud_number VARCHAR(8) NOT NULL, " +
                            "stud_first_name VARCHAR(50) NOT NULL, " +
                            "stud_last_name VARCHAR(50) NOT NULL, " +
                            "stud_birth_date DATE NOT NULL, " +
                            "stud_level level, " +
                            "stud_average_grade FLOAT" +
                            ")");
        }
    }
}
