package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController {
    private static final String DATABASE_URL = "jdbc:h2:tcp://localhost/~/test";
    public MainController() {
        initDatabase();
    }

    private void initDatabase() {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl(), "sa", "pass")) {
            connection.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS games (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "result VARCHAR(50) NOT NULL)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveGameResult(String result) {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl(), "sa", "pass")) {
            connection.createStatement().executeUpdate(
                    "INSERT INTO games (result) VALUES ('" + result + "')"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void showGameResult() {
        try (Connection connection = DriverManager.getConnection(getDatabaseUrl(), "sa", "pass")) {
            connection.createStatement().executeUpdate(
                    "SELECT * FROM games"
            );
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static String getDatabaseUrl() {
		return DATABASE_URL;
	}

	public ResultSet getPreviousGameResults() {
	    try {
	        Connection connection = DriverManager.getConnection(getDatabaseUrl(), "sa", "pass");
	        String query = "SELECT result FROM games";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        return preparedStatement.executeQuery();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
