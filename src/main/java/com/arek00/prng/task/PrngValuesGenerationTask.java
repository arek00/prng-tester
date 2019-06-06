package com.arek00.prng.task;

import com.arek00.prng.db.PrngRepositoryService;
import com.modp.random.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PrngValuesGenerationTask {

    @Value("${db.prng.maxBatchSize:1000}")
    private Integer maxBatchSize;

    @Value("${prng.sampleSize:1000}")
    private int sampleSize;

    @Value("${db.prng.tablePrefix:_}")
    private String tableNamePrefix;

    @Value("${prng.variable.bitwise:1000}")
    private int bitwise;

    private final PrngRepositoryService service;
    private int runCount = 0;

    @Autowired
    public PrngValuesGenerationTask(final PrngRepositoryService service) {
        this.service = service;
    }

    public void run(final RandomGenerator rng) {
        log.info("Generating rng values with: " + rng.getClass().getSimpleName());
        final String suffix = tableNameSuffix(rng);
        final String tableName = String.format("%s_%s_%s", this.tableNamePrefix, suffix, getRunCount());
        service.createPrngTable(tableName);

        generationLoop(rng, sampleSize, maxBatchSize, tableName);
    }

    private void generationLoop(final RandomGenerator rng, final int sampleSize, final int maxBatchSize, final String tableName) {
        final List<String> buffer = new ArrayList<>();

        IntStream.range(0, sampleSize).forEach(iteration -> {
            final String generatedValue = Integer.toHexString(rng.next(bitwise));
            buffer.add(generatedValue);

            if (iteration % maxBatchSize == 0 && iteration > 0) {
                flushBuffer(buffer, tableName);
            }
        });

        flushBuffer(buffer, tableName);
    }

    private void flushBuffer(final List<String> buffer, final String tableName) {
        log.info("Inserting " + buffer.size() + " to database");
        service.insertValues(tableName, buffer);
        buffer.clear();
    }

    private synchronized int getRunCount() {
        return runCount++;
    }

    private String tableNameSuffix(final RandomGenerator generator) {
        final String classSimpleName = generator.getClass().getSimpleName();

        return classSimpleName.replaceAll("[a-z]", "").toLowerCase();
    }

}
