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
        return null;
    }

    @Override
    public Credential create(Credential dto) {
        return null;
    }

    @Override
    public void delete(int id) {

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
