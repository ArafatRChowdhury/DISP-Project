package io.camunda.demo.process_order;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateShipmentWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateShipmentWorker.class);

    @JobWorker(type = "create-shipment")
    public void createShipment(final ActivatedJob job, final JobClient client) {
        try {
            String customerId   = getRequiredVariable(job, "customerId");
            String productName  = getRequiredVariable(job, "productName");
            int quantity        = Integer.parseInt(getRequiredVariable(job, "quantity"));

            // In production this would call a courier/shipment API to book delivery
            String shipmentId = "SHIP-" + customerId + "-" + System.currentTimeMillis();

            LOGGER.info("Shipment created for customer: {}, product: {}, quantity: {}, shipmentId: {}",
                    customerId, productName, quantity, shipmentId);

            client.newCompleteCommand(job.getKey())
                    .variable("shipmentId", shipmentId)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Create shipment failed for job {}: {}", job.getKey(), e.getMessage());
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