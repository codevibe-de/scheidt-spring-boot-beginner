package pizza.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.product.ProductService;

import java.time.DayOfWeek;
import java.util.Arrays;

// exercise a): unit test without Spring -- no context, the dependencies are mocks
class OrderServiceUnitTest {

    // TODO a) mocks for OrderRepository, CustomerService and ProductService
    OrderRepository orderRepository;

    CustomerService customerService;

    ProductService productService;

    OrderService orderService;

    Customer customer;

    @BeforeEach
    void setUp() {
        // discount on all weekdays so that the test does not depend on today's day
        OrderProperties orderProperties = new OrderProperties(
                30,
                Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).toList(),
                10.0);
        orderService = new OrderService(orderRepository, orderProperties, customerService, productService);

        // the database assigns the id otherwise
        customer = new Customer("Enrico Pallazzo", null, "+49 123 456789");
        ReflectionTestUtils.setField(customer, "id", 42L);

        // TODO a) customerService shall return this customer for "+49 123 456789"
    }

    @Test
    void placeOrder_appliesDiscount() {
        // TODO a) price 11.00 for 2 x P-10, save() returns the order passed in;
        //         expected: 9.90, the counter was increased, the order saved
    }

    @Test
    void placeOrderForUnknownProduct_savesNothing() {
        // TODO a) the ProductService throws a ProductNotFoundException;
        //         expected: the exception, the counter was increased, the OrderRepository never used
    }
}
