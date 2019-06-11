package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DistributionTest extends AbstractPrngTest {

    private static final String DISTRIBUTION_TEST_OUTPUT = "distribution";
    private static final String QUERY_PATTERN = "SELECT bucket, count(bucket) \n" +
            "FROM (SELECT ((%1$s %% (%2$d/2)) + (%2$d/2)) as bucket \n" +
            "FROM %3$s) \n" +
            "AS buckets \n" +
            "GROUP BY bucket \n" +
            "ORDER BY count DESC;";

    public DistributionTest(final PrngRepositoryService repositoryService) {
        super(repositoryService, DISTRIBUTION_TEST_OUTPUT);
    }

    @Override
    protected String createQuery(String tableName, GenerationConfig config, String outputDirectory) {
        log.info("Start performing distribution test data.");
        log.info("Table: " + tableName + " output directory: " + outputDirectory + "\n" +
                "Config: " + config.toString());

        return String.format(QUERY_PATTERN, config.getColumnName(), config.getSampleSize(), tableName);
    }
}
