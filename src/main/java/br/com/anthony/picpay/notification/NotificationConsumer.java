package br.com.anthony.picpay.notification;

import br.com.anthony.picpay.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private final RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("\"localhost:8080/api/notification")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {
        var response = restClient.get().retrieve().toEntity(Notification.class);
        if (response.getStatusCode().isError() || !response.getBody().message()) {
            throw new NotificationException("Error to send notification.");
        }
    }
}
