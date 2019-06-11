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
public class DuplicationTest extends AbstractPrngTest {

    private static final String OUTPUT_DIRECTORY = "duplication";

    private static final String QUERY_PATTERN =
            "SELECT %1$s, count(%1$s) " +
                    "FROM %2$s " +
                    "GROUP BY %1$s " +
                    "HAVING count(%1$s) > 1 " +
                    "ORDER BY count(%1$s) DESC;";

    public DuplicationTest(final PrngRepositoryService service) {
        super(service, OUTPUT_DIRECTORY);
    }

    @Override
    protected String createQuery(String tableName, GenerationConfig config, String outputDir) {
        log.info("Start performing duplication test.");
        log.info("Table: " + tableName + " output directory: " + outputDir + "\n" +
                "Config: " + config.toString());

        return String.format(QUERY_PATTERN, config.getColumnName(), tableName);
    }
}
