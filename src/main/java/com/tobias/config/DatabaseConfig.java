package com.tobias.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private DatabaseConfig() {
    }

    private static String getEnv(String key) {
        String systemValue = System.getenv(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        return DOTENV.get(key);
    }

    public static String getUrl() {
        String host = getEnv("DB_HOST");
        String port = getEnv("DB_PORT");
        String db = getEnv("POSTGRES_DB");

        return "jdbc:postgresql://" + host + ":" + port + "/" + db;
    }

    public static String getUser() {
        return getEnv("POSTGRES_USER");
    }

    public static String getPassword() {
        return getEnv("POSTGRES_PASSWORD");
    }

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(getUrl(), getUser(), getPassword());

            System.out.println("Conectado no banco!");
            return conn;

        } catch (Exception e) {
            System.out.println("Erro ao conectar no banco:");
            e.printStackTrace();
            return null;
        }
    }
}
