package com.example.camundaworker;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculatePriceWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatePriceWorker.class);

    @JobWorker(type = "calculate-price")
    public void calculatePrice(final ActivatedJob job, final JobClient client) {
        try {
            String productName     = getRequiredVariable(job, "productName");
            String productCategory = getRequiredVariable(job, "productCategory");
            int quantity           = Integer.parseInt(getRequiredVariable(job, "quantity"));

            // In production this would look up real pricing from a database or pricing service
            double unitPrice    = productCategory.equalsIgnoreCase("tool") ? 29.99 : 49.99;
            double totalPrice   = unitPrice * quantity;

            LOGGER.info("Price calculated for product: {}, quantity: {}, total: {}",
                    productName, quantity, totalPrice);

            client.newCompleteCommand(job.getKey())
                    .variable("price", totalPrice)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Calculate price failed for job {}: {}", job.getKey(), e.getMessage());
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
