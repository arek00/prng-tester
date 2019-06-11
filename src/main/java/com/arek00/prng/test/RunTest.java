package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;

public class RunTest extends AbstractPrngTest {

    private static final String RUN_TEST_OUTPUT = "run_test";
    private static final String pattern = "SELECT sum(sequence_end) FROM (\n" +
            "SELECT *, \n" +
            "CASE WHEN lag(diff, 1) OVER (ORDER BY id ASC) = diff then 0\n" +
            "ELSE 1\n" +
            "END AS sequence_end FROM\n" +
            "(SELECT *, " +
            "(%1$s - LAG(%1$s, 1) OVER(ORDER BY id ASC)) / ABS(%1$s - (LAG(%1$s, 1) OVER(ORDER BY id ASC))) AS DIFF " +
            "FROM %2$s) AS diffs) AS sequences_ends;";

    public RunTest(final PrngRepositoryService repositoryService) {
        super(repositoryService, RUN_TEST_OUTPUT);
    }

    @Override
    protected String createQuery(String tableName, GenerationConfig generationConfig, String outputDir) {
        return String.format(pattern, generationConfig.getColumnName(), tableName);
    }
}
