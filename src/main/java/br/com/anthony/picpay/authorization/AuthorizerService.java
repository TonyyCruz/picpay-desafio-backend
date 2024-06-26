package br.com.anthony.picpay.authorization;

import br.com.anthony.picpay.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);
    final private RestClient restClient;

    public AuthorizerService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8080/api/authorization")
                .build();
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("authorizing transaction {}...", transaction);
        var response = restClient.get().retrieve().toEntity(Authorization.class);
        if (response.getStatusCode().isError() || !response.getBody().isAuthorized()) {
            throw new UnauthorizedTransactionException("Unauthorized transaction.");
        }
        LOGGER.info("transaction authorized {}...", transaction);
    }
}
