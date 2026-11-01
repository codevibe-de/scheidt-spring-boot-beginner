package pizza.delivery;

import org.springframework.stereotype.Component;

/**
 * Fragt den Lieferdienst (siehe {@code src/services/java/DeliveryService.java}) nach der
 * Lieferzeit für eine Postleitzahl.
 */
@Component
public class DeliveryClient {

    // Übung a): einen RestClient für app.delivery.base-url bauen und GET /estimates aufrufen
    public int getDeliveryTimeInMinutes(String postalCode) {
        throw new UnsupportedOperationException("Übung a)");
    }
}
