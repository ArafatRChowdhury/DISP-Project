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
public class PayForCustomerWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayForCustomerWorker.class);

    @JobWorker(type = "process-payment") // MUST match your BPMN taskDefinition
    public void processPayment(final ActivatedJob job, final JobClient client) {

        LOGGER.info("Processing payment...");

        // 🔹 Get variables from BPMN
        Map<String, Object> variables = job.getVariablesAsMap();

        double amount = (double) variables.getOrDefault("price", 0.0);

        // 🔹 Simple mock payment logic
        boolean paymentSuccess;

        if (amount > 0) {
            paymentSuccess = true; // simulate success
        } else {
            paymentSuccess = false;
        }

        // 🔹 Prepare result variables
        Map<String, Object> result = new HashMap<>();
        result.put("paymentStatus", paymentSuccess ? "SUCCESS" : "FAILED");

        LOGGER.info("Payment result: " + result.get("paymentStatus"));

        // 🔹 Complete the job and return variables
        client.newCompleteCommand(job.getKey())
                .variables(result)
                .send()
                .join();
    }
}
