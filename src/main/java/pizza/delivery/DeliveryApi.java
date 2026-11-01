package pizza.delivery;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Bonus c): der Lieferdienst als HTTP-Interface. Die Implementierung erzeugt Spring Boot,
 * siehe {@code @ImportHttpServices} an {@code PizzaApp}; die Basis-URL steht in
 * {@code spring.http.serviceclient.delivery.base-url}.
 */
@HttpExchange("/estimates")
public interface DeliveryApi {

    @GetExchange
    DeliveryEstimate getEstimate(@RequestParam String postalCode);
}
