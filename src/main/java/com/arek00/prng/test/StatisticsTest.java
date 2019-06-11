package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;

public class StatisticsTest extends AbstractPrngTest {
    private static final String pattern = "SELECT var_pop(%1$s) as VARIANCE, avg(%1$s) as MEAN FROM %2$s;";
    private static final String STATISTICS_TEST_OUPUT = "basic_stats";

    public StatisticsTest(final PrngRepositoryService repositoryService) {
        super(repositoryService, STATISTICS_TEST_OUPUT);
    }

    @Override
    protected String createQuery(String tableName, GenerationConfig generationConfig, String outputDir) {
        return String.format(pattern, generationConfig.getColumnName(), tableName);
    }
}
