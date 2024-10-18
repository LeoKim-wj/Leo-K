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
    private Connection connect() {
        String url = "jdbc:sqlite:game_data.db";  // SQLite DB 파일 경로
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void saveGame(String cardStates, int tryCount, int successCount) {
        String sql = "INSERT INTO game_state (cardStates, tryCount, successCount) VALUES (?, ?, ?)";

        try (Connection conn = connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardStates);
            pstmt.setInt(2, tryCount);
            pstmt.setInt(3, successCount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet loadGame() {
        String sql = "SELECT * FROM game_state ORDER BY id DESC LIMIT 1";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void createTable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
