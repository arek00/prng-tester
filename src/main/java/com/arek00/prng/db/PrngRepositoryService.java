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
                                 @Value("${db.cleanup:false}") final boolean cleanUp) {
        this.repository = repository;

        if (cleanUp) {
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

    public void insertValues(final String tableName, final List<String> values) {
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

        } catch (SQLException e) {
            log.error("Problem during cleaning up database. " + e.getMessage());
        }
    }
}
