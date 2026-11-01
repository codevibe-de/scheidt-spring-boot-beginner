package pizza.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// exercise b): full context, but the ProductService is a mock and the CustomerService a spy
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "app.order.discount-days=")
class OrderServiceMockBeanTest {

    @Autowired
    OrderService orderService;

    // TODO b) ProductService as a mock, CustomerService as a spy

    @Test
    void placeOrder_usesPriceFromProductService() {
        // TODO b) price 10.00 for 1 x P-10, order for "+49 123 456789";
        //         expected: 10.00 and exactly one call of increaseOrderCount()
    }

    @Test
    void placeOrderForUnknownProduct_countsAttemptOnce() {
        // TODO b) the ProductService throws a ProductNotFoundException for every order;
        //         order for "+1 77 551237"; expected: exactly one call of increaseOrderCount(),
        //         and the counter in the database went up by one
    }
}
