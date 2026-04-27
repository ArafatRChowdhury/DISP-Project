package com.example.camundaworker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DecommissionToolWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecommissionToolWorker.class);

    @JobWorker(type = "decommission-tool")
    public void decommissionTool(final ActivatedJob job, final JobClient client) {
        try {
            String productName = getRequiredVariable(job, "productName");
            String customerId  = getRequiredVariable(job, "customerId");

            // In production this would update the IMS to mark the tool as decommissioned
            // and remove it from active inventory permanently
            LOGGER.info("Tool: {} decommissioned, previously hired by customer: {}",
                    productName, customerId);

            client.newCompleteCommand(job.getKey())
                    .variable("toolDecommissioned", true)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Decommission tool failed for job {}: {}", job.getKey(), e.getMessage());
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
