package com.arek00.prng.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Repository
public class PrngRepository {

    private final Connection connection;
    private final Config config;
    private final PrngQueriesFactory queriesFactory;


    @Autowired
    public PrngRepository(final Config config) {
        this.config = config;
        this.connection = config.getConnection();
        this.queriesFactory = new PrngQueriesFactory(config);
    }

    public void insertRandomValues(final String tableName, final List<String> values) {
        final String query = queriesFactory.batchInsert(tableName, values);
        try {
            System.out.println(query);
            execute(query);
        } catch (SQLException e) {

            log.error("Problem during batch insert. " + e.getMessage());
        }
    }

    public void createPrngTable(final String tableName) throws SQLException {
        final String query = queriesFactory.createTableQuery(tableName);

        System.out.println(query);

        execute(query);
    }

    public ResultSet getTablesInSchema(final String schemaName) throws SQLException {
        final String getTablesQuery = queriesFactory.getTables(schemaName);

        return executeWithResult(getTablesQuery);
    }


    public void dropTables(final List<String> tableNames) throws SQLException {
        final String query = queriesFactory.dropTables(tableNames);
        execute(query);
    }


    private void execute(final String query) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.execute(query);
    }

    private ResultSet executeWithResult(final String query) throws SQLException {
        final Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

}
