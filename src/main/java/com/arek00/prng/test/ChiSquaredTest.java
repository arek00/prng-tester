package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChiSquaredTest extends AbstractPrngTest {
    private static final String TEST_OUTPUT_DIR = "chi_squared";

    private static final String pattern = "SELECT sum(chi_square + missed_values_sum) as chi_square_sum FROM \n" +
            "(SELECT bucket, chi_square, bucket - (lag(bucket, 1) OVER (ORDER BY bucket ASC)) - 1  AS missed_values_sum FROM\n" +
            "(SELECT bucket, POwER((count - 1), 2) as chi_square FROM \n" +
            "(SELECT bucket, count(bucket) as count FROM \n" +
            "(SELECT (%1$s %% CAST(%2$d as BIGINT)) as bucket FROM %3$s) AS buckets GROUP BY bucket) AS counts) AS squares ) AS missing_chi_squares;";

    public ChiSquaredTest(final PrngRepositoryService repositoryService) {
        super(repositoryService, TEST_OUTPUT_DIR);
    }

    @Override
    protected String createQuery(String tableName, GenerationConfig config, String outputDirectory) {
        log.info("Start performing chi squared test.");
        log.info("Table: " + tableName + " output directory: " + outputDirectory + "\n" +
                "Config: " + config.toString());

        return String.format(pattern, config.getColumnName(), config.getSampleSize(), tableName);

    }
}
