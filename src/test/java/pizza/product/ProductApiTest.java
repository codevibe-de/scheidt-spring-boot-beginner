package pizza.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

// Bonus d): dasselbe Interface, das der Controller implementiert, als Client gegen die laufende App
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiTest {

    @LocalServerPort
    int port;

    ProductApi client;

    @BeforeEach
    void setUp() {
        RestClient restClient = RestClient.create("http://localhost:" + port);
        client = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(ProductApi.class);
    }

    @Test
    void getProduct() {
        Product product = client.getProduct("P-11");

        Assertions.assertThat(product.getName()).isEqualTo("Pizza Capricciosa");
    }

    @Test
    void getProducts() {
        Assertions.assertThat(client.getProducts())
                .extracting(Product::getId)
                .contains("P-10", "P-11", "P-12");
    }
}
