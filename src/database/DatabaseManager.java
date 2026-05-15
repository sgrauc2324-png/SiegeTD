package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getTopTimes(int level) throws SQLException {
        List<String> times = new ArrayList<>();
        String sql = "SELECT u.username, g.time FROM game g " +
                "JOIN users u ON g.user_id = u.id " +
                "WHERE g.level = ? ORDER BY g.time ASC LIMIT 3";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, level);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    times.add(rs.getString("username") + " - " + rs.getString("time"));
                }
            }
        }
        return times;
    }
}