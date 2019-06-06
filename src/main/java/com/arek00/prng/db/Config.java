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
public class Config {

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
    @Getter
    private final Connection connection;

    public Config(@Value("${db.host}") String host,
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

    private Connection initializeConnection() {
        try {
            final String uri = createJdbcConnectionUri(host, port, database);
            log.info("Connect to db: " + uri);
            return DriverManager.getConnection(uri, userName, password);
        } catch (SQLException e) {
            log.error("Could not initialize. " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        throw new IllegalStateException("Illegal state of application. Connection should be returned or application exited.");
    }

    private String createJdbcConnectionUri(final String host, final Integer port, final String database) {
        final String postgresUrlPattern = "jdbc:postgresql://%s:%s/%s";

        return String.format(postgresUrlPattern, host, port, database);
    }
}



