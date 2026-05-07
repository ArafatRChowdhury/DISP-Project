package io.camunda.demo.process_order;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateImsStockLevelWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateImsStockLevelWorker.class);

    @JobWorker(type = "update-ims-stock-level")
    public void updateImsStockLevel(final ActivatedJob job, final JobClient client) {

        String productName = job.getVariable("productName").toString();
        int quantity = Integer.parseInt(job.getVariable("quantity").toString());

        // In production this would call the IMS (Inventory Management System) API
        // to decrement stock. For now we log and confirm the update.
        int updatedStockLevel = 100 - quantity; // Simulated remaining stock
        LOGGER.info("IMS stock updated — product: {}, reduced by: {}, remaining stock: {}",
                productName, quantity, updatedStockLevel);

        client.newCompleteCommand(job.getKey())
                .variable("updatedStockLevel", updatedStockLevel)
                .send()
                .join();
    }
}