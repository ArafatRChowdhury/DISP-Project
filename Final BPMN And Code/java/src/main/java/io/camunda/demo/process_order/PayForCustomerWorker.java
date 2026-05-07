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
public class PayForCustomerWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayForCustomerWorker.class);

    @JobWorker(type = "pay-for-customer")
    public void payForCustomer(final ActivatedJob job, final JobClient client) {
        try {
            String customerId = getRequiredVariable(job, "customerId");
            double price = (double) job.getVariable("price");
            double financeAmount = price / 12;

            // Fintrust covering the cost on behalf of the customer
            boolean paymentMade = financeAmount > 0;
            LOGGER.info("Fintrust paying for customer: {}, amount: {}", customerId, financeAmount);

            Map<String, Object> result = new HashMap<>();
            result.put("financeAmount", financeAmount);
            result.put("fintrustPaymentMade", paymentMade);

            client.newCompleteCommand(job.getKey())
                    .variables(result)
                    .send()
                    .join();

        } catch (Exception e) {
            LOGGER.error("Pay for customer failed for job {}: {}", job.getKey(), e.getMessage());
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