package com.moffd.app.Dao;

import com.moffd.app.Models.Credential;
import com.moffd.app.Models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements DaoInterface<User> {
    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User update(User dto) {
        return null;
    }

    @Override
    public User create(User dto) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    private User recreateUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String masterPassword = rs.getString("master_password");
        String email = rs.getString("email");

        return new User(id, username, masterPassword, email);
    }

}

