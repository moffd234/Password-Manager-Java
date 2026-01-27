package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CredentialsDao implements DaoInterface<Credential> {
    private Connection connection = ConnectionFactory.getConnection();

    @Override
    public Credential findById(int id) {
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM credentials WHERE id = ?")){
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
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

    @Override
    public Credential recreateDTO(ResultSet rs) {
        return null;
    }
}
