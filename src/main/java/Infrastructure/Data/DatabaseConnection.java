package Infrastructure.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = System.getenv().getOrDefault(
        "DB_URL",
        "jdbc:mysql://localhost:3306/ECommerceDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
    );
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("MySQL JDBC Driver not found.", e);
                }
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to open database connection.", e);
        }
        return connection;
    }
}
