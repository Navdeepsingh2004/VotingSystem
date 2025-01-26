package com.example.votingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/VotingSystem";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "navdeep2004"; // Change to your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}