// Slide "HTTP-Interfaces" (150), the part "without Spring Boot"
package c150.httpinterface;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pizza.delivery.DeliveryTimeApi;

public class ManualProxy {

    public static void main(String[] args) {
        // without Spring Boot: create the proxy by hand
        RestClient restClient =
                RestClient.create("http://localhost:8081");
        DeliveryTimeApi deliveryTimeApi = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(DeliveryTimeApi.class);

        System.out.println(deliveryTimeApi.getEstimate("40302"));
    }
}
