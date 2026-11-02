// Slide "POST mit RestClient" (150)
package c150.post;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import pizza.order.OrderRequest;
import pizza.order.OrderResponse;

import java.util.Map;

public class OrderClient {

    private final RestClient restClient;

    public OrderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    void placeOrder() {
        OrderRequest request = new OrderRequest();
        request.phoneNumber = "+49 123 456789";
        request.itemQuantities = Map.of("P-11", 2);

        ResponseEntity<OrderResponse> response = restClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(OrderResponse.class);

        response.getStatusCode();   // 201 CREATED
        response.getBody().id();    // 2
    }
}
