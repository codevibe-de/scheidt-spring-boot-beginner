package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.product.ProductNotFoundException;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.util.Map;

// Übung b): voller Kontext, aber der ProductService ist ein Mock und der CustomerService ein Spy.
// Beides ändert den Kontext: Spring startet für diese Klasse einen eigenen statt den gecachten.
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "app.order.discount-days=")
class OrderServiceMockBeanTest {

    @Autowired
    OrderService orderService;

    @MockitoBean
    ProductService productService;

    @MockitoSpyBean
    CustomerService customerService;

    @Test
    void placeOrder_usesPriceFromProductService() {
        // given
        Mockito.when(productService.getTotalPrice(Map.of("P-10", 1))).thenReturn(new BigDecimal("10.00"));

        // when
        Order order = orderService.placeOrder("+49 123 456789", Map.of("P-10", 1));

        // then
        Assertions.assertThat(order.getTotalPrice()).isEqualByComparingTo("10.00");
        Mockito.verify(customerService).increaseOrderCount(order.getCustomer().getId());
    }

    @Test
    void placeOrderForUnknownProduct_countsAttemptOnce() {
        // given
        Customer customer = customerService.getCustomerByPhoneNumber("+1 77 551237");
        Mockito.when(productService.getTotalPrice(Mockito.anyMap()))
                .thenThrow(new ProductNotFoundException("X-99"));

        // when
        Assertions.assertThatThrownBy(() -> orderService.placeOrder("+1 77 551237", Map.of("X-99", 1)))
                .isInstanceOf(ProductNotFoundException.class);

        // then -- der Spy ruft die echte Methode auf und zeichnet den Aufruf auf
        Mockito.verify(customerService, Mockito.times(1)).increaseOrderCount(customer.getId());
        Assertions.assertThat(customerService.getCustomerByPhoneNumber("+1 77 551237").getOrderCount())
                .isEqualTo(customer.getOrderCount() + 1);
    }
}
