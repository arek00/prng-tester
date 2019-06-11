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

@Slf4j
public abstract class AbstractPrngTest implements PrngTest {

    private final PrngRepositoryService repositoryService;
    private final String testOutputDir;

    public AbstractPrngTest(final PrngRepositoryService repositoryService, final String testOutputDir) {
        this.repositoryService = repositoryService;
        this.testOutputDir = testOutputDir;
    }

    @Override
    public void runTest(final String tableName, final GenerationConfig config, final String outputDirectory) {
        final String query = createQuery(tableName, config, outputDirectory);
        final ResultSet result = repositoryService.executeWithResult(query);

        final File outputFile = OutputFileCreator.createFile(
                outputDirectory,
                testOutputDir,
                OutputFileCreator.withExtension(tableName, "csv"));

        try {
            CsvWriter.writeResultSet(result, outputFile);
        } catch (SQLException | IOException e) {
            final String testName = this.getClass().getSimpleName();
            log.error("Problem during " + testName + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract String createQuery(final String tableName, final GenerationConfig generationConfig, final String outputDir);
}
