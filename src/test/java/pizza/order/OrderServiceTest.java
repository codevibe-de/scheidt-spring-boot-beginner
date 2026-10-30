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
        // Rabatt deaktivieren, damit der erwartete Gesamtpreis tagesunabhängig (deterministisch) ist
        "app.order.discount-days="
})
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    void placeOrder() {
        // when -- die Preise kommen aus den Beispieldaten des DataLoader "sample":
        //         2 * 5.50 (Pizza Margarita) + 1 * 7.90 (Salat Italiano) = 18.90
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

        // und: die Bestellung ist gespeichert und wird wieder gefunden
        Assertions.assertThat(orderService.getOrders()).contains(order);
    }

    // Bonus e)
    @Test
    void getOrdersAndRevenueOfCustomer() {
        // given -- zwei Bestellungen von Elizabeth Magie: 2 * 7.50 = 15.00 und 1 * 8.90 = 8.90
        orderService.placeOrder("+1 77 551237", Map.of("P-11", 2));
        orderService.placeOrder("+1 77 551237", Map.of("S-03", 1));

        // then
        Assertions.assertThat(orderService.getOrdersOfCustomer("+1 77 551237"))
                .hasSize(2)
                .allSatisfy(order -> Assertions.assertThat(order.getCustomer().getFullName()).isEqualTo("Elizabeth Magie"));
        Assertions.assertThat(orderService.getTotalRevenueOfCustomer("+1 77 551237")).isEqualByComparingTo("23.90");
        Assertions.assertThat(orderService.getTotalRevenueOfCustomer("0000")).isEqualByComparingTo("0");
    }

    @Test
    void placeOrderForUnknownCustomer() {
        Assertions.assertThatThrownBy(() -> orderService.placeOrder("0000", Map.of("P-10", 1)))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
