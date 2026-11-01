package pizza.delivery;

import org.springframework.stereotype.Component;

/**
 * Fragt den Lieferzeit-Dienst (siehe {@code src/services/java/DeliveryTimeService.java}) nach der
 * Lieferzeit für eine Postleitzahl.
 */
@Component
public class DeliveryTimeClient {

    // Übung a): einen RestClient für app.delivery-time.base-url bauen und GET /estimates aufrufen
    public int getDeliveryTimeInMinutes(String postalCode) {
        throw new UnsupportedOperationException("Übung a)");
    }
}
