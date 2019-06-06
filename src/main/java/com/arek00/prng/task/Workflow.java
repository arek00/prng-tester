package com.arek00.prng.task;

import com.modp.random.BlumBlumShub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.lang.Math.toIntExact;

@Service
public class Workflow {

    private final PrngValuesGenerationTask generationTask;

    @Autowired
    public Workflow(final PrngValuesGenerationTask generationTask) {
        this.generationTask = generationTask;
    }

    @PostConstruct
    public void run() {
        final Integer bits = toIntExact((System.currentTimeMillis() % 1024));


        final BlumBlumShub generator = new BlumBlumShub(bits);

        generationTask.run(generator);
    }

}
