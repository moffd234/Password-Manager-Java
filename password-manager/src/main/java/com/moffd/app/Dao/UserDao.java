package com.moffd.app.Dao;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends BaseDao implements DaoInterface<User> {

    private final IOConsole console = new IOConsole();

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? recreateUser(rs) : null;
            }
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? recreateUser(rs) : null;
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        ArrayList<User> output = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                output.add(recreateUser(rs));
            }

            return output;
        }
    }

    @Override
    public User update(User dto) throws SQLException {
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

            return rowsUpdated != 0 ? dto : null;
        }
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
                return null;
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    dto.setId(rs.getInt(1));
                } else {
                    return null;
                }
            }

            return dto;
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
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

