package database;

import java.sql.*;

import java.sql.*;

public final class DatabaseManager {
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3307/siegetd";
    private final String user = "root";
    private final String password = "mysql";

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void createUser(String username) throws SQLException {
        String sql = "INSERT INTO users (username, actual_level) VALUES (?, 1)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }

    public User loadUser(String username) throws SQLException {
        String sql = "SELECT id, username, actual_level FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getInt("actual_level")
                    );
                }
            }
        }
        return null;
    }

    public void insertGame(Game game) throws SQLException {
        String sql = "INSERT INTO game (user_id, level, time) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, game.getUserId());
            ps.setInt(2, game.getLevel());
            ps.setString(3, game.getTime());
            ps.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
