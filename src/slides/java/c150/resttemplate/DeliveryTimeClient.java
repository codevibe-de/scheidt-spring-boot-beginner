// Slide "WebClient und RestTemplate" (150), only the RestTemplate part;
// WebClient needs spring-boot-starter-webclient, which the project does not have
package c150.resttemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import pizza.delivery.DeliveryEstimate;

public class DeliveryTimeClient {

    private final RestTemplateBuilder restTemplateBuilder;
    private final String baseUrl;

    public DeliveryTimeClient(RestTemplateBuilder restTemplateBuilder,
            @Value("${app.delivery-time.base-url}") String baseUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.baseUrl = baseUrl;
    }

    DeliveryEstimate getEstimate(String postalCode) {
        // RestTemplate: Starter spring-boot-starter-restclient
        RestTemplate restTemplate = restTemplateBuilder
                .baseUri(baseUrl).build();
        DeliveryEstimate estimate = restTemplate.getForObject(
                "/estimates?postalCode={postalCode}",
                DeliveryEstimate.class, postalCode);
        return estimate;
    }
}
