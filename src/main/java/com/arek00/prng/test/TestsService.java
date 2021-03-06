package com.arek00.prng.test;

import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.db.PrngRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TestsService {

    private final PrngRepositoryService repositoryService;

    private final String outputDirectory;

    private final TestsConfig testsConfig;

    private final List<PrngTest> tests;

    private final GenerationConfig generationConfig;

    @Autowired
    public TestsService(final PrngRepositoryService repositoryService,
                        final TestsConfig testsConfig,
                        final GenerationConfig generationConfig) {
        this.repositoryService = repositoryService;
        this.outputDirectory = testsConfig.getOutputDirectory();
        this.testsConfig = testsConfig;
        this.tests = initializeTests(testsConfig, repositoryService);
        this.generationConfig = generationConfig;
    }

    private List<PrngTest> initializeTests(final TestsConfig testsConfig, final PrngRepositoryService service) {
        final List<PrngTest> tests = new ArrayList<>();

        tests.add(new DuplicationTest(service));
        tests.add(new DistributionTest(service));
        tests.add(new ChiSquaredTest(service));
        tests.add(new RunTest(service));

        return tests;
    }

    public void performTests(final List<String> tables) {
        tests.forEach(test -> {
            tables.forEach(table -> test.runTest(table, generationConfig, outputDirectory));
        });
    }
}
