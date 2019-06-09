package com.arek00.prng.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GenerationConfig {

    @Value("${db.prng.batchSize:1000}")
    private Integer batchSize;

    @Value("${db.prng.sampleSize:1000}")
    private int sampleSize;

    @Value("${db.prng.tablePrefix:_}")
    private String tableNamePrefix;

    @Value("${db.prng.variable.bitwise:1000}")
    private int bitwise;

    @Value("${db.prng.tablesNumber:1}")
    private int tablesNumber;

    @Value("${db.prng.columnName}")
    private String columnName;
}
