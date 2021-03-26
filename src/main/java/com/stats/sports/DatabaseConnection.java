package com.stats.sports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A connection to the sports statistics database.
 *
 * <p>Purdue University -- CS34800 -- Spring 2021 -- Project</p>
 *
 * @author Logan Kulinski, lbk@purdue.edu
 * @version March 26, 2021
 */
public final class DatabaseConnection {
    /**
     * The connection of this database connection.
     */
    private static Connection connection;

    /**
     * Returns the connection of this database connection.
     *
     * @return the connection of this database connection
     */
    public static Connection getConnection() {
        String url;
        String username;
        String password;

        if (DatabaseConnection.connection != null) {
            return DatabaseConnection.connection;
        } //end if

        url = System.getProperty("url");

        username = System.getProperty("username");

        password = System.getProperty("password");

        if ((url == null) || (username == null) || (password == null)) {
            DatabaseConnection.connection = null;

            return null;
        } //end if

        try {
            DatabaseConnection.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();

            DatabaseConnection.connection = null;

            return null;
        } //end try catch

        return DatabaseConnection.connection;
    } //getConnection
}