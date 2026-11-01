package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import pizza.customer.CustomerNotFoundException;

import java.util.Map;

// @SpringBootTest boots via SpringApplication so the DataLoadRunner runs and seeds the data;
// @ActiveProfiles("test") excludes the noisy LogicRunner (see @Profile("!test") on it).
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        // disable the discount so that the expected total price does not depend on the day (deterministic)
        "app.order.discount-days="
})
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    void placeOrder() {
        // when -- the prices come from the sample data of the DataLoader "sample":
        //         2 * 5.50 (Pizza Margarita) + 1 * 7.90 (Italian Salad) = 18.90
        Order order = orderService.placeOrder(
                "+49 123 456789",
                Map.of(
                        "P-10", 2,
                        "S-02", 1
                )
        );

        // then
        Assertions.assertThat(order.getId()).isNotNull();
        Assertions.assertThat(order.getCustomer().getFullName()).isEqualTo("Enrico Pallazzo");
        Assertions.assertThat(order.getTotalPrice()).isEqualByComparingTo("18.90");

        // and: the order is saved and found again
        Assertions.assertThat(orderService.getOrders()).contains(order);
    }

    @Test
    void placeOrderForUnknownCustomer() {
        Assertions.assertThatThrownBy(() -> orderService.placeOrder("0000", Map.of("P-10", 1)))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
