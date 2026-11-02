// Slide "GET mit RestClient" (150)
package c150.get;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import pizza.delivery.DeliveryEstimate;
import pizza.product.Product;

import java.util.List;

public class DeliveryTimeClient {

    private final RestClient restClient;

    public DeliveryTimeClient(RestClient restClient) {
        this.restClient = restClient;
    }

    DeliveryEstimate getEstimate(String postalCode) {
        DeliveryEstimate estimate = restClient.get()
                .uri("/estimates?postalCode={postalCode}", postalCode)
                .retrieve()
                .body(DeliveryEstimate.class);
        return estimate;
    }

    ResponseEntity<DeliveryEstimate> getEstimateEntity(String postalCode) {
        // with status and headers
        ResponseEntity<DeliveryEstimate> entity = restClient.get()
                .uri("/estimates?postalCode={postalCode}", postalCode)
                .retrieve()
                .toEntity(DeliveryEstimate.class);
        return entity;
    }

    List<Product> getProducts() {
        // lists
        List<Product> products = restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return products;
    }
}
