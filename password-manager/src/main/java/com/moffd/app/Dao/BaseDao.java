package com.moffd.app.Dao;

import com.moffd.app.Utils.ConnectionFactory;

import java.sql.Connection;

public abstract class BaseDao {
    Connection getConnection() {
        return ConnectionFactory.getConnection();
    }
}
