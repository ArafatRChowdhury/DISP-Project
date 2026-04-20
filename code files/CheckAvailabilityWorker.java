package com.example.camundaworker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;

import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CheckAvailabilityWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckAvailabilityWorker.class);
    @JobWorker(type="check-availability")
    public void checkAvailability(final ActivatedJob job, final JobClient client) {
        //when a database is implemented, this would check that database for the tool
        //for now just assume that the tool is always available
        String productCategory = job.getVariable("productCategory").toString();
        String productName = job.getVariable("productName").toString();
        int quantity = (int) job.getVariable("quantity");
        String transactionType = job.getVariable("transactionType").toString();
        boolean toolAvailable;

        if (quantity > 0) {
            toolAvailable = true;
        } else {
            toolAvailable = false;
        }

        client.newCompleteCommand(job.getKey())
                .variables(toolAvailable)
                .send()
                .join();
    }
}

