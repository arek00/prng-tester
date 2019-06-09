package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;
import com.arek00.prng.utils.CsvWriter;
import com.arek00.prng.utils.OutputFileCreator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.arek00.prng.utils.OutputFileCreator.createFile;
import static com.arek00.prng.utils.OutputFileCreator.withExtension;

@Slf4j
public class DistributionTest implements PrngTest {

    private static final String OUTPUT_DIR = "distribution";
    private static final String QUERY_PATTERN = "SELECT bucket, count(bucket) \n" +
            "FROM (SELECT ((%1$s %% (%2$d/2)) + (%2$d/2)) as bucket \n" +
            "FROM %3$s) \n" +
            "AS buckets \n" +
            "GROUP BY bucket \n" +
            "ORDER BY count DESC;";

    private final PrngRepositoryService repositoryService;
    private final int bucketRange;

    public DistributionTest(final PrngRepositoryService repositoryService, final int bucketRange) {
        this.bucketRange = bucketRange;
        this.repositoryService = repositoryService;
    }

    @Override
    public void runTest(String tableName, GenerationConfig config, String outputDirectory) {
        final String query = String.format(QUERY_PATTERN, config.getColumnName(), bucketRange, tableName);
        final ResultSet result = repositoryService.executeWithResult(query);
        final File outputFile = createFile(outputDirectory, OUTPUT_DIR, withExtension(tableName, "csv"));

        printToCsv(result, outputFile);
    }

    private void printToCsv(final ResultSet resultSet, final File outputFile) {
        try {
            CsvWriter.writeResultSet(resultSet, outputFile);
        } catch (SQLException e) {
            log.error("SQLException. " + e.getMessage());
        } catch (IOException e) {
            log.error("IOException. " + e.getMessage());
        }
    }
}
