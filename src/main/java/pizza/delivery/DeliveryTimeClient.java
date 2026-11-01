package pizza.delivery;

import org.springframework.stereotype.Component;

/**
 * Asks the delivery time service (see {@code src/services/java/DeliveryTimeService.java}) for the
 * delivery time to a postal code.
 */
@Component
public class DeliveryTimeClient {

    // exercise a): build a RestClient for app.delivery-time.base-url and call GET /estimates
    public int getDeliveryTimeInMinutes(String postalCode) {
        throw new UnsupportedOperationException("exercise a)");
    }
}
