package com.arek00.prng.db;

import com.arek00.prng.configuration.DbConnectionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PrngRepository {

    private final Connection connection;
    private final DbConnectionConfig config;
    private final PrngQueriesFactory queriesFactory;


    @Autowired
    public PrngRepository(final DbConnectionConfig config) {
        this.config = config;
        this.connection = config.getConnection();
        this.queriesFactory = new PrngQueriesFactory();
    }

    public void insertRandomValues(final String tableName, final List<Long> values) {
        final String query = queriesFactory.batchInsert(tableName, values);
        try {
            execute(query);
        } catch (SQLException e) {

            log.error("Problem during batch insert. " + e.getMessage());
        }
    }

    public void createPrngTable(final String tableName) throws SQLException {
        final String query = queriesFactory.createTableQuery(tableName);

        execute(query);
    }

    public ResultSet getTablesInSchema(final String schemaName) throws SQLException {
        final String getTablesQuery = queriesFactory.getTables(schemaName);

        return executeWithResult(getTablesQuery);
    }


    public void dropTables(final List<String> tableNames) throws SQLException {
        final List<String> queries = tableNames.stream()
                .map(queriesFactory::dropTable)
                .collect(Collectors.toList());

        for (final String query : queries) {
            execute(query);
        }
    }


    public void execute(final String query) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.execute(query);
    }

    public ResultSet executeWithResult(final String query) throws SQLException {
        final Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public ResultSet getGeneratedTablesNames(final String pattern) throws SQLException {
        final String queryPattern = queriesFactory.getTablesByPattern(pattern);

        return executeWithResult(queryPattern);
    }

}
