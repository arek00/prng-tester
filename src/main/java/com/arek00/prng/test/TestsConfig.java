package com.arek00.prng.test;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TestsConfig {

    @Value("${reports.duplicationTest.dir}")
    private String outputDirectory;

    @Value("${tests.duplication.bucketsNumber}")
    private Integer bucketsNumber;
}
