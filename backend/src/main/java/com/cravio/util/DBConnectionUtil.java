package com.cravio.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Centralized database connection utility using HikariCP connection pool.
 * Reads configuration from environment variables with sensible defaults.
 */
public class DBConnectionUtil {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        String host = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String port = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
        String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "Cravio";
        String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
        String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "";

        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Returns a connection from the HikariCP pool.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DBConnectionUtil() {
        // Prevent instantiation
    }
}
