package pizza.order;

import org.assertj.core.api.Assertions;
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

// exercise 120: every test creates its own customer so that the orders of the other tests
// do not distort its counter. The counter is read fresh from the database, i.e. the way
// another transaction sees it after the commit.
@SpringBootTest
@ActiveProfiles("test")
class OrderCountTest {

    @Autowired
    OrderService orderService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void placeOrder_increasesOrderCount() {
        // given
        Customer customer = createCustomer();

        // when
        orderService.placeOrder(customer.getPhoneNumber(), Map.of("P-10", 1));

        // then
        Assertions.assertThat(orderCountOf(customer)).isEqualTo(1);
    }

    @Test
    void placeOrderForUnknownProduct_increasesOrderCountAnyway() {
        // given
        Customer customer = createCustomer();

        // when -- the product does not exist, the order fails
        Assertions.assertThatThrownBy(() -> orderService.placeOrder(customer.getPhoneNumber(), Map.of("X-99", 1)))
                .isInstanceOf(ProductNotFoundException.class);

        // then -- the attempt is counted anyway
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
