package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CredentialDao extends BaseDao implements DaoInterface<Credential> {

    @Override
    public Credential findById(int id) throws SQLException {
        String sql = "SELECT * FROM credentials WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return recreateCredential(rs);
                }

            }
        }

        return null;
    }

    @Override
    public List<Credential> findAll() throws SQLException {
        ArrayList<Credential> output = new ArrayList<>();
        String sql = "SELECT * FROM credentials";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                output.add(recreateCredential(rs));
            }

            return output;
        }
    }

    public List<Credential> findAllForUser(User user) throws SQLException {
        List<Credential> output = new ArrayList<>();
        String sql = "SELECT * FROM credentials WHERE user_id = ? ORDER BY site";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    output.add(recreateCredential(rs));
                }

            }

        }

        return output;
    }

    @Override
    public Credential update(Credential dto) throws SQLException {
        String sql = "UPDATE credentials " +
                "SET user_id = ?, site = ?, site_username = ?, site_password = ?, iv = ? " +
                "WHERE id = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dto.getUserId());
            statement.setString(2, dto.getSite());
            statement.setString(3, dto.getSiteUsername());
            statement.setString(4, dto.getSitePassword());
            statement.setBytes(5, dto.getIv());
            statement.setInt(6, dto.getId());

            int linesChanged = statement.executeUpdate();

            if (linesChanged == 0) {
                return null;
            }

            return dto;

        }
    }

    @Override
    public Credential create(Credential dto) throws SQLException {
        String sql = "INSERT INTO credentials (user_id, site, site_username, site_password, iv) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, dto.getUserId());
            statement.setString(2, dto.getSite());
            statement.setString(3, dto.getSiteUsername());
            statement.setString(4, dto.getSitePassword());
            statement.setBytes(5, dto.getIv());

            int linesChanged = statement.executeUpdate();

            if (linesChanged == 0) {
                return null;
            }

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                dto.setId(rs.getInt(1));
            } else {
                return null;
            }

            return dto;

        }

    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM credentials WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

        }
    }

    private Credential recreateCredential(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String site = rs.getString("site");
        String siteUsername = rs.getString("site_username");
        String sitePassword = rs.getString("site_password");
        byte[] iv = rs.getBytes("iv");

        return new Credential(id, userId, site, siteUsername, sitePassword, iv);
    }
}
