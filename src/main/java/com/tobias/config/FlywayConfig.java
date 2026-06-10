package com.tobias.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

    private FlywayConfig() {
    }

    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        DatabaseConfig.getUrl(),
                        DatabaseConfig.getUser(),
                        DatabaseConfig.getPassword()
                )
                .locations("classpath:bd/migration")
                .load();

        flyway.migrate();
        System.out.println("Migracoes do Flyway aplicadas com sucesso!");
    }
}
