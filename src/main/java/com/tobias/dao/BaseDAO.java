package com.tobias.dao;

import java.sql.Connection;

import com.tobias.config.DatabaseConfig;

public abstract class BaseDAO {

    protected Connection getConnection() {
        return DatabaseConfig.connect();
    }
}
