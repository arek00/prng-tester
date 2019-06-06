package com.arek00.prng.db;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class PrngQueriesFactory {
    private final Config config;

    @Getter
    private static final String columnName = "prng_value";

    public PrngQueriesFactory(final Config config) {
        this.config = config;
    }

    public String createTableQuery(final String tableName) {
        final String pattern = "CREATE TABLE IF NOT EXISTS %s (\n" +
                "id SERIAL PRIMARY KEY,\n" +
                "%s TEXT);";

        return String.format(pattern, tableName, columnName);
    }

    public String insertInto(final String value, final String table) {
        final String pattern = "INSERT INTO %s (%s) VALUES('%s');";

        return String.format(pattern, table, columnName, value);
    }

    public String batchInsert(final String table, final List<String> values) {
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

    public String dropTables(final List<String> tables) {
        final StringBuilder queryBuilder = new StringBuilder();
        final String pattern = "DROP TABLE %s;";

        queryBuilder.append("BEGIN;\n");

        tables.stream()
                .map(table -> String.format(pattern, table))
                .forEach(drop -> queryBuilder.append(drop).append("\n"));
        queryBuilder.append("COMMIT;");

        return queryBuilder.toString();
    }
}
