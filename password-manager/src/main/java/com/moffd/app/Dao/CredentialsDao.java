package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class CredentialsDao implements DaoInterface<Credential> {
    private Connection connection = ConnectionFactory.getConnection();

    @Override
    public Credential findById(int id) {
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
