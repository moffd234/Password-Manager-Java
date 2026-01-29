package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CredentialDao implements DaoInterface<Credential> {

    @Override
    public Credential findById(int id) {
        String sql = "SELECT * FROM credentials WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return recreateCredential(rs);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error getting credential by ID " + e);
        }
        return null;
    }

    @Override
    public List<Credential> findAll() {
        ArrayList<Credential> output = new ArrayList<>();
        String sql = "SELECT * FROM credentials";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                output.add(recreateCredential(rs));
            }

            return output;

        } catch (SQLException e) {
            System.out.println("Error getting all credentials " + e);
        }

        return output;
    }

    @Override
    public Credential update(Credential dto) {
        String sql = "UPDATE credentials " +
                "SET user_id = ?, site = ?, site_username = ?, site_password = ? " +
                "WHERE id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dto.getUserId());
            statement.setString(2, dto.getSite());
            statement.setString(3, dto.getSiteUsername());
            statement.setString(4, dto.getSitePassword());
            statement.setInt(5, dto.getId());

            int linesChanged = statement.executeUpdate();

            if (linesChanged == 0) {
                System.out.println("Error updating credential. No changes made");
                return null;
            }

            return dto;

        } catch (SQLException e) {
            System.out.println("Error updating credential");
        }
        return null;
    }

    @Override
    public Credential create(Credential dto) {
        String sql = "INSERT INTO credentials (user_id, site, site_username, site_password) " +
                "VALUES (?, ?, ?, ?);";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, dto.getUserId());
            statement.setString(2, dto.getSite());
            statement.setString(3, dto.getSiteUsername());
            statement.setString(4, dto.getSitePassword());

            int linesChanged = statement.executeUpdate();

            if (linesChanged == 0) {
                System.out.println("Error inserting credential. No credential inserted");
                return null;
            }

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                dto.setId(rs.getInt(1));
            } else {
                System.out.println("Error setting credential ID");
            }

            return dto;

        } catch (SQLException e) {
            System.out.println("Error inserting credential " + e);
        }

        return null;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM credentials WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsModified = statement.executeUpdate();

            if(rowsModified == 0){
                System.out.println("Error deleting credential. Id not found");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting credential: " + e);
        }
    }

    private Credential recreateCredential(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String site = rs.getString("site");
        String siteUsername = rs.getString("site_username");
        String sitePassword = rs.getString("site_password");

        return new Credential(id, userId, site, siteUsername, sitePassword);
    }

    private Connection getConnection() {
        return ConnectionFactory.getConnection();
    }
}
