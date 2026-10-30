package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pizza.customer.Customer;
import pizza.customer.CustomerRepository;
import pizza.customer.CustomerService;
import pizza.product.ProductNotFoundException;

import java.util.Map;
import java.util.UUID;

// Übung 120: Jeder Test legt einen eigenen Kunden an, damit die Bestellungen der anderen Tests
// seinen Zähler nicht verfälschen. Gelesen wird der Zähler frisch aus der Datenbank, also so,
// wie ihn eine andere Transaktion nach dem Commit sieht.
@SpringBootTest
@ActiveProfiles("test")
class OrderCountTest {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Disabled("Übung a)")
    @Test
    void placeOrder_increasesOrderCount() {
        // given
        Customer customer = createCustomer();

        // when
        orderService.placeOrder(customer.getPhoneNumber(), Map.of("P-10", 1));

        // then
        Assertions.assertThat(orderCountOf(customer)).isEqualTo(1);
    }

    @Disabled("Übung a)")
    @Test
    void placeOrderForUnknownProduct_increasesOrderCountAnyway() {
        // given
        Customer customer = createCustomer();

        // when -- das Produkt gibt es nicht, die Bestellung scheitert
        Assertions.assertThatThrownBy(() -> orderService.placeOrder(customer.getPhoneNumber(), Map.of("X-99", 1)))
                .isInstanceOf(ProductNotFoundException.class);

        // then -- der Versuch zählt trotzdem
        Assertions.assertThat(orderCountOf(customer)).isEqualTo(1);
    }

    private Customer createCustomer() {
        Customer customer = customerService.createCustomer(
                new Customer("Trans Action", null, UUID.randomUUID().toString().substring(0, 30)));
        Assertions.assertThat(orderCountOf(customer)).isZero();
        return customer;
    }

    private int orderCountOf(Customer customer) {
        return customerRepository.findById(customer.getId()).orElseThrow().getOrderCount();
    }
}
