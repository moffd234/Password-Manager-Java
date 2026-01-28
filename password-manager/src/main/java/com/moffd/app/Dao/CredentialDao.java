package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CredentialDao implements DaoInterface<Credential> {
    private Connection connection = ConnectionFactory.getConnection();

    @Override
    public Credential findById(int id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM credentials WHERE id = ?")) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return recreateDTO(rs);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error getting credential by ID " + e);
        }
        return null;
    }

    @Override
    public List<Credential> findAll() {
        return List.of();
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

    private Credential recreateDTO(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String site = rs.getString("site");
        String siteUsername = rs.getString("site_username");
        String sitePassword = rs.getString("site_password");

        return new Credential(id, userId, site, siteUsername, sitePassword);
    }
}
