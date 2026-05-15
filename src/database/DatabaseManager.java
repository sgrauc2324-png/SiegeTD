package database;

import java.sql.*;

public final class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/siegetd", "root", "mysql");
    }

    public User loadUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getInt("actual_level"));
            }
        }
        return null;
    }

    public void createUser(String username) throws SQLException {
        String sql = "INSERT INTO users (username, actual_level) VALUES (?, 1)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    public void saveGame(Game game) throws SQLException {
        String sql = "INSERT INTO game (user_id, level, time) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, game.userId);
            ps.setInt(2, game.level);
            ps.setString(3, game.time);
            ps.executeUpdate();
        }
    }

    public void updateUserLevel(int userId, int newLevel) throws SQLException {
        String sql = "UPDATE users SET actual_level = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newLevel);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}