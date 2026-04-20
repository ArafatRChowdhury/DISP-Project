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
public class CalculatePriceWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatePriceWorker.class);
    @JobWorker(type="calculate-price")
    public void calculatePrice(final ActivatedJob job, final JobClient client) {
        //until a database is implemented, just have this as the price
        //when the database is implemented, get the tool/material price from that
        double price = 10.00;
        client.newCompleteCommand(job.getKey())
                .variables(price)
                .send()
                .join();
    }
}
