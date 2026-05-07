package Infrastructure.Repositories;

import java.sql.Connection;
import Infrastructure.Data.DatabaseConnection;

public abstract class BaseRepository {
    
    protected Connection connection;

    public BaseRepository() {
        this.connection = DatabaseConnection.getConnection();
        if (this.connection == null) {
            throw new IllegalStateException("Database connection was not initialized.");
        }
    }
}
