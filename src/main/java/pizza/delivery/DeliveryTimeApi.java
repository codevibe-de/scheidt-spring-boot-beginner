package pizza.delivery;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Bonus c): the delivery time service as an HTTP interface. Spring Boot creates the implementation,
 * see {@code @ImportHttpServices} on {@code PizzaApp}; the base URL is set in
 * {@code spring.http.serviceclient.delivery-time.base-url}.
 */
@HttpExchange("/estimates")
public interface DeliveryTimeApi {

    @GetExchange
    DeliveryEstimate getEstimate(@RequestParam String postalCode);
}
