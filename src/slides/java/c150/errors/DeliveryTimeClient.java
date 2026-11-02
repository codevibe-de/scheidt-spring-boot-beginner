// Slide "Fehler beim Aufruf" (150)
package c150.errors;

import org.springframework.web.client.RestClient;
import pizza.delivery.DeliveryEstimate;

public class DeliveryTimeClient {

    private final RestClient restClient;

    public DeliveryTimeClient(RestClient restClient) {
        this.restClient = restClient;
    }

    DeliveryEstimate getEstimate(String postalCode) {
        DeliveryEstimate estimate = restClient.get()
                .uri("/estimates?postalCode={postalCode}", postalCode)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        (request, response) -> {
                            throw new NoDeliveryAreaException(postalCode);
                        })
                .body(DeliveryEstimate.class);
        return estimate;
    }
}
