package pizza.delivery;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Bonus c): der Lieferzeit-Dienst als HTTP-Interface. Die Implementierung erzeugt Spring Boot,
 * siehe {@code @ImportHttpServices} an {@code PizzaApp}; die Basis-URL steht in
 * {@code spring.http.serviceclient.delivery-time.base-url}.
 */
@HttpExchange("/estimates")
public interface DeliveryTimeApi {

    @GetExchange
    DeliveryEstimate getEstimate(@RequestParam String postalCode);
}
