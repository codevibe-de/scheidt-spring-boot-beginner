package pizza.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// Übung b): voller Kontext, aber der ProductService ist ein Mock und der CustomerService ein Spy
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "app.order.discount-days=")
class OrderServiceMockBeanTest {

    @Autowired
    OrderService orderService;

    // TODO b) ProductService als Mock, CustomerService als Spy

    @Test
    void placeOrder_usesPriceFromProductService() {
        // TODO b) Preis 10.00 für 1 x P-10, Bestellung für "+49 123 456789";
        //         erwartet: 10.00 und genau ein Aufruf von increaseOrderCount()
    }

    @Test
    void placeOrderForUnknownProduct_countsAttemptOnce() {
        // TODO b) der ProductService wirft für jede Bestellung eine ProductNotFoundException;
        //         Bestellung für "+1 77 551237"; erwartet: genau ein Aufruf von increaseOrderCount(),
        //         und der Zähler in der Datenbank ist um eins gestiegen
    }
}
