package com.arek00.prng.test;

import com.arek00.prng.db.PrngQueriesFactory;
import com.arek00.prng.db.PrngRepositoryService;
import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.utils.CsvWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.arek00.prng.utils.OutputFileCreator.createFile;
import static com.arek00.prng.utils.OutputFileCreator.withExtension;

@Slf4j
public class DuplicationTest implements PrngTest {

    private static final String OUTPUT_DIRECTORY = "duplication";

    private static final String QUERY_PATTERN =
            "SELECT %1$s, count(%1$s) " +
                    "FROM %2$s " +
                    "GROUP BY %1$s " +
                    "HAVING count(%1$s) > 1 " +
                    "ORDER BY count(%1$s) DESC;";

    private final PrngRepositoryService repositoryService;

    public DuplicationTest(final PrngRepositoryService service) {
        this.repositoryService = service;
    }

    public void runTest(final String tableName, final GenerationConfig config, final String outputDir) {
        final String columnName = config.getColumnName();
        final String query = String.format(QUERY_PATTERN, columnName, tableName);
        final ResultSet resultSet = repositoryService.executeWithResult(query);
        writeCsv(resultSet, tableName, outputDir);
    }

    private void writeCsv(final ResultSet resultSet, final String tableName, final String outputDir) {
        try {
            final File outputFile = createFile(outputDir, OUTPUT_DIRECTORY, withExtension(tableName, "csv"));
            CsvWriter.writeResultSet(resultSet, outputFile);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
