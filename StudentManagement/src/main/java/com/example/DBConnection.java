
package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection()
            throws SQLException {

        String host = System.getenv()
                .getOrDefault("DB_HOST", "localhost");

        String port = System.getenv()
                .getOrDefault("DB_PORT", "3306");

        String database = System.getenv()
                .getOrDefault("DB_NAME", "student_db");

        String username = System.getenv()
                .getOrDefault("DB_USER", "root");

        String password = System.getenv()
                .getOrDefault("DB_PASSWORD", "root");

        String url = "jdbc:mysql://" + host + ":"
                + port + "/" + database
                + "?useSSL=false"
                + "&allowPublicKeyRetrieval=true"
                + "&serverTimezone=UTC";

        return DriverManager.getConnection(
                url, username, password);
    }
}
