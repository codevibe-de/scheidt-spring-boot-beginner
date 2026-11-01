package pizza.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Fragt den Lieferdienst (siehe {@code src/services/java/DeliveryService.java}) nach der
 * Lieferzeit für eine Postleitzahl.
 */
@Component
public class DeliveryClient {

    private final RestClient restClient;

    private final DeliveryApi deliveryApi;

    public DeliveryClient(RestClient.Builder restClientBuilder,
                          @Value("${app.delivery.base-url}") String baseUrl,
                          DeliveryApi deliveryApi) {
        // der Builder von Boot kennt schon die Message-Converter und die Timeouts
        // aus spring.http.clients.*
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.deliveryApi = deliveryApi;
    }

    // Bonus c): über das HTTP-Interface
    public int getDeliveryTimeInMinutes(String postalCode) {
        return this.deliveryApi.getEstimate(postalCode).minutes();
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
