package com.arek00.prng.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PrngRepositoryService {
    private static final String TABLE_NAME_COLUMN = "tablename";

    private final PrngRepository repository;

    @Autowired
    public PrngRepositoryService(final PrngRepository repository,
                                 @Value("${db.cleanUpOnStart:false}") final boolean cleanUp,
                                 @Value("${db.prng.onlyTests:true}") final boolean onlyTests) {
        this.repository = repository;

        if (cleanUp && !onlyTests) {
            cleanUpTables();
        }
    }

    public void createPrngTable(final String tableName) {
        try {
            log.info("Creating table: " + tableName);
            repository.createPrngTable(tableName);
        } catch (SQLException e) {
            log.error("Could not create table " + tableName + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertValues(final String tableName, final List<Long> values) {
        repository.insertRandomValues(tableName, values);
    }

    public void cleanUpTables() {
        log.info("Cleaning up tables.");

        try {
            final ResultSet rs = repository.getTablesInSchema("public");
            final List<String> tableNames = new ArrayList<>();

            while (rs.next()) {
                tableNames.add(rs.getString(TABLE_NAME_COLUMN));
            }

            repository.dropTables(tableNames);

        } catch (SQLException e) {
            log.error("Problem during cleaning up database. " + e.getMessage());
        }
    }

    public void executeQuery(final String query) {
        try {
            repository.execute(query);
        } catch (SQLException e) {
            log.error("Could not execute query. " + e.getMessage());
        }
    }

    public ResultSet executeWithResult(final String query) {
        try {
            return repository.executeWithResult(query);
        } catch (SQLException e) {
            log.error("Could not execute query. " + e.getMessage());
        }

        return null;
    }

    public List<String> getTableNames(final String tableNamePattern) {
        try {
            final List<String> tableNames = new ArrayList<>();
            final ResultSet resultSet = repository.getGeneratedTablesNames(tableNamePattern);

            while (resultSet.next()) {
                tableNames.add(resultSet.getString(TABLE_NAME_COLUMN));
            }

            return tableNames;

        } catch (SQLException e) {
            log.error("Error during get generated tables names. " + e.getMessage());
        }

        return new ArrayList<>();
    }
}
