package pizza.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.product.ProductService;

import java.time.DayOfWeek;
import java.util.Arrays;

// Übung a): Unit-Test ohne Spring -- kein Kontext, die Abhängigkeiten sind Mocks
class OrderServiceUnitTest {

    // TODO a) Mocks für OrderRepository, CustomerService und ProductService
    OrderRepository orderRepository;

    CustomerService customerService;

    ProductService productService;

    OrderService orderService;

    Customer customer;

    @BeforeEach
    void setUp() {
        // Rabatt an allen Wochentagen, damit der Test nicht vom heutigen Tag abhängt
        OrderProperties orderProperties = new OrderProperties(
                30,
                Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).toList(),
                10.0);
        orderService = new OrderService(orderRepository, orderProperties, customerService, productService);

        // die Id vergibt sonst die Datenbank
        customer = new Customer("Enrico Pallazzo", null, "+49 123 456789");
        ReflectionTestUtils.setField(customer, "id", 42L);

        // TODO a) customerService soll für "+49 123 456789" diesen Kunden liefern
    }

    @Test
    void placeOrder_appliesDiscount() {
        // TODO a) Preis 11.00 für 2 x P-10, save() gibt die übergebene Bestellung zurück;
        //         erwartet: 9.90, der Zähler wurde erhöht, die Bestellung gespeichert
    }

    @Test
    void placeOrderForUnknownProduct_savesNothing() {
        // TODO a) der ProductService wirft eine ProductNotFoundException;
        //         erwartet: die Exception, der Zähler wurde erhöht, das OrderRepository nie benutzt
    }
}
