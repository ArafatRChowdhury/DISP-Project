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
public class ProcessPaymentWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentWorker.class);

    @JobWorker(type = "process-payment")
    public void processPayment(final ActivatedJob job, final JobClient client) {
        try {
            String customerId   = getRequiredVariable(job, "customerId");
            String paymentMethod = getRequiredVariable(job, "paymentMethod");
            String fulfilmentMethod = getRequiredVariable(job, "fulfilmentMethod");
            double price        = Double.parseDouble(getRequiredVariable(job, "price"));

            // In production this would call a real payment gateway
            boolean paymentApproved = true;//price > 0;
            System.out.println("Testing payment");

            LOGGER.info("Payment processed for customer: {}, method: {}, amount: {}, approved: {}",
                    customerId, paymentMethod, price, paymentApproved);

            Map<String, Object> result = new HashMap<>();
            result.put("paymentMethod", paymentMethod);
            result.put("customerId", customerId);
            result.put("paymentApproved", paymentApproved);
            result.put("price", price);
            result.put("fulfilmentMethod", fulfilmentMethod);

            client.newCompleteCommand(job.getKey())
                    .variables(result)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Process payment failed for job {}: {}", job.getKey(), e.getMessage());
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