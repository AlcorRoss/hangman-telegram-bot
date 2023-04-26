package com.alcorross.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final HikariConfig CONFIG = new HikariConfig();
    private static final HikariDataSource DS;
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";

    static {
        CONFIG.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        CONFIG.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        CONFIG.setUsername(PropertiesUtil.get(USERNAME_KEY));
        CONFIG.setMaximumPoolSize(Integer.parseInt(PropertiesUtil.get(POOL_SIZE_KEY)));
        DS = new HikariDataSource(CONFIG);
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }
}
