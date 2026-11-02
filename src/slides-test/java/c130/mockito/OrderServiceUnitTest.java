// Slides "Unit-Test mit Mockito", "Mocks programmieren" and "Aufrufe prüfen" (130)
package c130.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pizza.customer.CustomerService;
import pizza.order.Order;
import pizza.order.OrderProperties;
import pizza.order.OrderRepository;
import pizza.order.OrderService;
import pizza.product.ProductNotFoundException;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    // not on the slide
    static final List<String> ALL_DAYS =
            Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).toList();

    @Mock OrderRepository orderRepository;
    @Mock CustomerService customerService;
    @Mock ProductService productService;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        // discount on all days, whatever today is
        var properties = new OrderProperties(
                30, ALL_DAYS, 10.0);
        orderService = new OrderService(orderRepository,
                properties, customerService, productService);
    }

    // slide "Mocks programmieren": snippets, not a runnable test
    void programMocks() {
        // set the return value
        Mockito.when(productService.getTotalPrice(Map.of("P-10", 2)))
                .thenReturn(new BigDecimal("11.00"));

        // throw an exception
        Mockito.when(productService.getTotalPrice(Map.of("X-99", 1)))
                .thenThrow(new ProductNotFoundException("X-99"));

        // return the argument passed in
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // for any arguments
        Mockito.when(productService.getTotalPrice(Mockito.anyMap()))
                .thenThrow(new ProductNotFoundException("X-99"));
    }

    // slide "Aufrufe prüfen": snippets, not a runnable test
    void verifyCalls() {
        // exactly once, with this argument
        Mockito.verify(customerService).increaseOrderCount(42L);

        // number of calls
        Mockito.verify(customerService, Mockito.times(1))
                .increaseOrderCount(42L);
        Mockito.verify(orderRepository, Mockito.atLeast(2))
                .save(Mockito.any());
        Mockito.verify(orderRepository, Mockito.never())
                .save(Mockito.any());

        // no calls at all
        Mockito.verifyNoInteractions(orderRepository);
    }
}
