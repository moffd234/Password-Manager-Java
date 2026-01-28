package com.moffd.app.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        final String url = "jdbc:mysql://localhost:3306/PWD_MANAGER";
        final String username = "dan";
        final String password = System.getenv("DB_PWD");

        if (password == null) {
            System.out.println("Error getting db pwd");
            return null;
        }

        try{
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Error creating db connection " + e);
        }

        return null;
    }
}
