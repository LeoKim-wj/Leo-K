/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorygame;

/**
 *
 * @author kmkm3
 */
import java.sql.*;

public class GameDB {
    private static final String URL = "jdbc:derby:memorygameDB;create=true";  // Derby DB URL
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public GameDB() {
        loadDriver(); 
    }

    private void loadDriver() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot find the Derby JDBC driver.", e);
        }
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void createTable() {
    String sql = 
        "CREATE TABLE GameState ("
        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
        + "cardStates VARCHAR(16) NOT NULL, "
        + "tryCount INT NOT NULL, "
        + "successCount INT NOT NULL"
        + ")";

    try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
        stmt.execute(sql);
    } catch (SQLException e) {
        if (!e.getSQLState().equals("X0Y32")) { 
            throw new RuntimeException("failed to create the table: " + e.getMessage(), e);
        }
    }
}

    public ResultSet loadGame() throws SQLException {
        String query = "SELECT * FROM GameState ORDER BY id DESC FETCH FIRST 1 ROW ONLY";
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public void saveGame(String cardStates, int tryCount, int successCount) {
        String sql = "INSERT INTO GameState (cardStates, tryCount, successCount) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardStates);
            pstmt.setInt(2, tryCount);
            pstmt.setInt(3, successCount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("failed to save the game: " + e.getMessage(), e);
        }
    }
}