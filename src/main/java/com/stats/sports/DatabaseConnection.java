package com.stats.sports;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public final class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        String url;
        String username;
        String password;

        if (connection != null) {
            return connection;
        } //end if

        url = System.getProperty("url");

        username = System.getProperty("username");

        password = System.getProperty("password");

        if ((url == null) || (username == null) || (password == null)) {
            connection = null;

            return null;
        } //end if

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();

            connection = null;

            return null;
        } //end try catch

        return connection;
    } //getConnection
}