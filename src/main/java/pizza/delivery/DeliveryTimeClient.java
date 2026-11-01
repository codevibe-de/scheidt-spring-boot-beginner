package pizza.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Fragt den Lieferzeit-Dienst (siehe {@code src/services/java/DeliveryTimeService.java}) nach der
 * Lieferzeit für eine Postleitzahl.
 */
@Component
public class DeliveryTimeClient {

    private final RestClient restClient;

    private final DeliveryTimeApi deliveryTimeApi;

    public DeliveryTimeClient(RestClient.Builder restClientBuilder,
                              @Value("${app.delivery-time.base-url}") String baseUrl,
                              DeliveryTimeApi deliveryTimeApi) {
        // der Builder von Boot kennt schon die Message-Converter und die Timeouts
        // aus spring.http.clients.*
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.deliveryTimeApi = deliveryTimeApi;
    }

    // Bonus c): über das HTTP-Interface
    public int getDeliveryTimeInMinutes(String postalCode) {
        return this.deliveryTimeApi.getEstimate(postalCode).minutes();
    }

    // Übung a): dasselbe von Hand mit dem RestClient
    int getDeliveryTimeInMinutesWithRestClient(String postalCode) {
        DeliveryEstimate estimate = this.restClient.get()
                .uri("/estimates?postalCode={postalCode}", postalCode)
                .retrieve()
                .body(DeliveryEstimate.class);
        return estimate.minutes();
    }
}
