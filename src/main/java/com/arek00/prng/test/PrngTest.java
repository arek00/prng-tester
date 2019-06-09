package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;

public interface PrngTest {

    void runTest(final String tableName, final GenerationConfig config, final String outputDirectory);
}
