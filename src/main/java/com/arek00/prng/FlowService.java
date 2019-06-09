package com.arek00.prng;

import com.arek00.prng.db.PrngRepositoryService;
import com.arek00.prng.configuration.GenerationConfig;
import com.arek00.prng.task.BBSGenerationTask;
import com.arek00.prng.test.TestsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class FlowService {

    private final BBSGenerationTask generationTask;
    private final TestsService testsService;

    @Autowired
    public FlowService(final BBSGenerationTask generationTask,
                       final TestsService testsService) {
        this.generationTask = generationTask;
        this.testsService = testsService;
    }

    @PostConstruct
    private void runFlow() {
        log.info("Run generation task");
        final List<String> generatedTableNames = generationTask.run();
        testsService.performTests(generatedTableNames);

        log.info("Application flow finished.");
        System.exit(0);
    }
}
