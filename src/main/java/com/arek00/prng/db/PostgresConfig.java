package com.arek00.prng.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Configuration
public class PostgresConfig {

    @Getter
    private final String host;
    @Getter
    private final Integer port;
    @Getter
    private final String userName;
    @Getter
    private final String password;
    @Getter
    private final String database;

    private final Connection connection;

    public PostgresConfig(@Value("${db.host}") String host,
                          @Value("${db.port}") Integer port,
                          @Value("${db.user}") String userName,
                          @Value("${db.password}") String password,
                          @Value("${db.database}") String database) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.database = database;
        this.connection = initializeConnection();
    }

    @Bean("pg_connection")
    public Connection connection() {
        return connection;
    }

    private Connection initializeConnection() {
        try {
            final String uri = createJdbcConnectionUri(userName, password, database);
            return DriverManager.getConnection(uri);
        } catch (SQLException e) {
            log.error("Could not initialize ");
            System.exit(1);
        }

        throw new IllegalStateException("Illegal state of application. Connection should be returned or application exited.");
    }

    private String createJdbcConnectionUri(final String user, final String password, final String database) {
        final String postgresUrlPattern = "jdbc:postgresql://%s:%s/%s";

        return String.format(postgresUrlPattern, user, password, database);
    }
}



