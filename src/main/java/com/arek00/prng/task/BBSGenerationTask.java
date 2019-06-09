package com.arek00.prng.task;

import com.modp.random.BlumBlumShub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BBSGenerationTask {

    private final PrngValuesGenerationTask generationTask;

    @Value("${prng.primeBits:4}")
    private Integer primeBits;

    @Autowired
    public BBSGenerationTask(final PrngValuesGenerationTask generationTask) {
        this.generationTask = generationTask;
    }

    public List<String> run() {
        final BlumBlumShub generator = new BlumBlumShub(primeBits);
        return generationTask.run(generator);
    }

}
