package com.arek00.prng.task;

import com.arek00.prng.db.PrngRepositoryService;
import com.arek00.prng.configuration.GenerationConfig;
import com.modp.random.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PrngValuesGenerationTask {

    private final PrngRepositoryService service;
    private final GenerationConfig config;


    @Autowired
    public PrngValuesGenerationTask(final PrngRepositoryService service, final GenerationConfig config) {
        this.service = service;
        this.config = config;
    }

    public List<String> run(final RandomGenerator rng) {
       return generationLoop(rng, config.getTablesNumber(), config.getSampleSize(), config.getBatchSize());
    }

    private List<String> generationLoop(final RandomGenerator rng, final int tablesNumber, final int sampleSize, final int maxBatchSize) {
        final List<String> tables = new ArrayList<>();

        IntStream.range(0, tablesNumber).forEach(tableNumber -> {
            log.info("Generating rng values with: " + rng.getClass().getSimpleName());
            final String suffix = tableNameSuffix(rng);
            final String tableName = String.format("%s_%s_%s", config.getTableNamePrefix(), suffix, tableNumber);
            service.createPrngTable(tableName);
            tables.add(tableName);

            singleTableGenerationLoop(rng, sampleSize, maxBatchSize, tableName);
        });

        return tables;
    }

    private void singleTableGenerationLoop(final RandomGenerator rng,
                                           final int sampleSize,
                                           final int maxBatchSize,
                                           final String tableName) {
        final List<Long> buffer = new ArrayList<>();

        IntStream.range(0, sampleSize).forEach(iteration -> {
            final long generatedValue = rng.next(config.getBitwise());
            buffer.add(generatedValue);

            if (iteration % maxBatchSize == 0 && iteration > 0) {
                flushBuffer(buffer, tableName);
                log.info("Generating progress: " + (1.0 * iteration / sampleSize) * 100 + "%");
            }
        });

        flushBuffer(buffer, tableName);
    }

    private void flushBuffer(final List<Long> buffer, final String tableName) {
        log.info("Inserting " + buffer.size() + " to database");
        service.insertValues(tableName, buffer);
        buffer.clear();
    }

    private String tableNameSuffix(final RandomGenerator generator) {
        final String classSimpleName = generator.getClass().getSimpleName();

        return classSimpleName.replaceAll("[a-z]", "").toLowerCase();
    }
}
