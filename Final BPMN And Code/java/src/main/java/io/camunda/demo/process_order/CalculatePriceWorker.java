package io.camunda.demo.process_order;

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

    @JobWorker(type = "calculate-price")
    public void calculatePrice(final ActivatedJob job, final JobClient client) {
        try {
            // Retrieve the product details passed in from the BPMN process
            String productCategory = getRequiredVariable(job, "productCategory");
            int quantity = Integer.parseInt(getRequiredVariable(job, "quantity"));

            // In a real implementation this would query a database for the actual unit price
            // For now we simulate pricing based on product category
            double unitPrice;
            if (productCategory.equalsIgnoreCase("tool")) {
                unitPrice = 15.00; // Daily hire rate for tools
            } else if (productCategory.equalsIgnoreCase("material")) {
                unitPrice = 5.00; // Price per unit for materials
            } else {
                unitPrice = 10.00; // Default price for any other category
            }

            // Multiply the unit price by the quantity to get the total price
            double totalPrice = unitPrice * quantity;

            LOGGER.info("Price calculated — category: {}, quantity: {}, unit price: £{}, total: £{}",
                    productCategory, quantity, unitPrice, totalPrice);

            // Send the calculated price back to the BPMN process as a variable
            // so the next task in the process can use it
            client.newCompleteCommand(job.getKey())
                    .variable("price", totalPrice)
                    .send()
                    .join();

        } catch (Exception e) {
            // If anything goes wrong, notify Camunda the job has failed
            // rather than leaving it hanging indefinitely
            LOGGER.error("Calculate price failed for job {}: {}", job.getKey(), e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    // Helper method to safely retrieve a variable from the process
    // If the variable is missing it throws a clear error rather than a cryptic null pointer
    private String getRequiredVariable(ActivatedJob job, String variableName) {
        Object value = job.getVariable(variableName);
        if (value == null) {
            throw new IllegalStateException("Required process variable is missing: " + variableName);
        }
        return value.toString();
    }
}