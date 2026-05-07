package io.camunda.demo.process_order;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendDeliveryNotificationWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendDeliveryNotificationWorker.class);

    @JobWorker(type = "send-delivery-notification")
    public void sendDeliveryNotification(final ActivatedJob job, final JobClient client) {
        try {
            String customerId  = getRequiredVariable(job, "customerId");
            String shipmentId  = getRequiredVariable(job, "shipmentId");
            String productName = getRequiredVariable(job, "productName");

            // In production this would send an email/SMS via a notification service
            LOGGER.info("Delivery notification sent to customer: {}, shipmentId: {}, product: {}",
                    customerId, shipmentId, productName);

            client.newCompleteCommand(job.getKey())
                    .variable("notificationSent", true)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Send delivery notification failed for job {}: {}", job.getKey(), e.getMessage());
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