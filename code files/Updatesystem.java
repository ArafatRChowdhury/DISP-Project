package com.example.camundaworker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateSystemWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystemWorker.class);

    @JobWorker(type = "updateSystem")
    public void updateSystem(final ActivatedJob job, final JobClient client) {
        try {
            String customerId  = getRequiredVariable(job, "customerId");
            String productName = getRequiredVariable(job, "productName");
            String transactionType = getRequiredVariable(job, "transactionType");

            // In production this would update the internal tool hire management system
            // to record the tool as issued to this customer
            LOGGER.info("System updated — tool: {} issued to customer: {}, transaction type: {}",
                    productName, customerId, transactionType);

            client.newCompleteCommand(job.getKey())
                    .variable("systemUpdated", true)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Update system failed for job {}: {}", job.getKey(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    private String getRequiredVariable(ActivatedJob job, String variableName) {
        Object value = job.getVariable(variableName);
        if (value == null) {
            throw new IllegalStateException("Required process variable is missing: " + variableName);
        }
        return value.toString();
    }
}
