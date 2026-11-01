package pizza.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Asks the delivery time service (see {@code src/services/java/DeliveryTimeService.java}) for the
 * delivery time to a postal code.
 */
@Component
public class DeliveryTimeClient {

    private final RestClient restClient;

    private final DeliveryTimeApi deliveryTimeApi;

    public DeliveryTimeClient(RestClient.Builder restClientBuilder,
                              @Value("${app.delivery-time.base-url}") String baseUrl,
                              DeliveryTimeApi deliveryTimeApi) {
        // Boot's builder already knows the message converters and the timeouts
        // from spring.http.clients.*
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.deliveryTimeApi = deliveryTimeApi;
    }

    // bonus c): via the HTTP interface
    public int getDeliveryTimeInMinutes(String postalCode) {
        return this.deliveryTimeApi.getEstimate(postalCode).minutes();
    }

    // exercise a): the same by hand with the RestClient
    int getDeliveryTimeInMinutesWithRestClient(String postalCode) {
        DeliveryEstimate estimate = this.restClient.get()
                .uri("/estimates?postalCode={postalCode}", postalCode)
                .retrieve()
                .body(DeliveryEstimate.class);
        return estimate.minutes();
    }
}
