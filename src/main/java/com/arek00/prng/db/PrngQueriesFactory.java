package com.arek00.prng.db;

import com.arek00.prng.configuration.DbConnectionConfig;
import lombok.Getter;

import java.util.List;

public class PrngQueriesFactory {
    @Getter
    private static final String columnName = "prng_value";

    public String createTableQuery(final String tableName) {
        final String pattern = "CREATE TABLE IF NOT EXISTS %s (\n" +
                "id SERIAL PRIMARY KEY,\n" +
                "%s BIGINT);";

        return String.format(pattern, tableName, columnName);
    }

    public String insertInto(final Long value, final String table) {
        final String pattern = "INSERT INTO %s (%s) VALUES(%d);";

        return String.format(pattern, table, columnName, value);
    }

    public String batchInsert(final String table, final List<Long> values) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("BEGIN;").append("\n");

        values.stream()
                .map(value -> this.insertInto(value, table))
                .forEach(insert -> queryBuilder.append(insert).append("\n"));
        queryBuilder.append("COMMIT;");

        return queryBuilder.toString();
    }

    public String getTables(final String schemaName) {
        final String pattern = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = '%s';";
        return String.format(pattern, schemaName);
    }

    public String getTablesByPattern(final String tableNamePattern) {
        final String pattern = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname like '%s';";
        return String.format(pattern, tableNamePattern);
    }

    public String dropTable(final String table) {
        final StringBuilder queryBuilder = new StringBuilder();
        final String pattern = "DROP TABLE %s CASCADE;";

        return String.format(pattern, table);
    }
}
