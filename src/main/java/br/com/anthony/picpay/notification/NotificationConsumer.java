package br.com.anthony.picpay.notification;

import br.com.anthony.picpay.authorization.AuthorizerService;
import br.com.anthony.picpay.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);
    private final RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8080/api/notification")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-desafio-backend")
    public void receiveNotification(Transaction transaction) {
        LOGGER.info("notifying transaction {}...", transaction);
        var response = restClient.get().retrieve().toEntity(Notification.class);
        if (response.getStatusCode().isError() || !response.getBody().message()) {
            throw new NotificationException("Error to send notification.");
        }
        LOGGER.info("notification has been sent {}...", response.getBody());
    }
}
