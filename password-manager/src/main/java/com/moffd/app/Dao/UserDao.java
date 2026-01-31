package com.moffd.app.Dao;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao implements DaoInterface<User> {

    private final IOConsole console = new IOConsole();

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return recreateUser(rs);
                }

                console.printError("User id not found " + id);
            }

        } catch (SQLException e) {
            console.printError("Error finding user id " + e);
        }
        return null;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try(ResultSet rs = statement.executeQuery()) {

                return rs.next() ? recreateUser(rs) : null;
            }
        }
    }

    @Override
    public List<User> findAll() {
        ArrayList<User> output = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                output.add(recreateUser(rs));
            }

            if (output.isEmpty()) {
                console.printError("Error getting all users: No users found");
            }

            return output;

        } catch (SQLException e) {
            console.printError("Error getting all users: " + e);
        }

        return output;
    }

    @Override
    public User update(User dto) {
        String sql = "UPDATE users " +
                "SET username = ? , master_password = ?, email = ? " +
                "WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getMasterPassword());
            statement.setString(3, dto.getEmail());
            statement.setInt(4, dto.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                console.printError("Error updating rows. No rows updated");
                return null;
            }

            return dto;

        } catch (SQLException e) {
            console.printError("Error updating user");
        }

        return null;
    }

    @Override
    public User create(User dto) throws SQLException {
        String sql = "INSERT INTO users (username, master_password, email) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getMasterPassword());
            statement.setString(3, dto.getEmail());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                console.printError("Error inserting user. User not inserted");
                return null;
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setId(rs.getInt(1));
                } else {
                    console.printError("Error setting user ID");
                    return null;
                }
            }

            return dto;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalStateException("Username already exists");
        } catch (SQLException e) {
            throw new SQLException("Database error while creating user", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsModified = statement.executeUpdate();

            if (rowsModified == 0) {
                console.printError("Error deleting user. Id not found");
            }

        } catch (SQLException e) {
            console.printError("Error deleting user: " + e);
        }
    }

    private User recreateUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String masterPassword = rs.getString("master_password");
        String email = rs.getString("email");

        return new User(id, username, masterPassword, email);
    }

}

