package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResourceAccessException;
import pizza.customer.Address;
import pizza.customer.Customer;
import pizza.customer.CustomerService;
import pizza.delivery.DeliveryTimeClient;
import pizza.product.ProductNotFoundException;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

// Übung a): Unit-Test ohne Spring -- kein Kontext, die Abhängigkeiten sind Mocks
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    CustomerService customerService;

    @Mock
    ProductService productService;

    @Mock
    DeliveryTimeClient deliveryTimeClient;

    OrderService orderService;

    Customer customer;

    @BeforeEach
    void setUp() {
        // Rabatt an allen Wochentagen, damit der Test nicht vom heutigen Tag abhängt
        OrderProperties orderProperties = new OrderProperties(
                30,
                Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).toList(),
                10.0);
        orderService = new OrderService(orderRepository, orderProperties, customerService, productService, deliveryTimeClient);

        // die Id vergibt sonst die Datenbank
        customer = new Customer("Enrico Pallazzo", new Address("Wasserstr. 123", "40302", "Atlantis"), "+49 123 456789");
        ReflectionTestUtils.setField(customer, "id", 42L);
        Mockito.when(customerService.getCustomerByPhoneNumber("+49 123 456789")).thenReturn(customer);
    }

    @Test
    void placeOrder_appliesDiscount() {
        // given
        Mockito.when(productService.getTotalPrice(Map.of("P-10", 2))).thenReturn(new BigDecimal("11.00"));
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order order = orderService.placeOrder("+49 123 456789", Map.of("P-10", 2));

        // then -- 11.00 abzüglich 10 % Rabatt
        Assertions.assertThat(order.getTotalPrice()).isEqualByComparingTo("9.90");
        Assertions.assertThat(order.getCustomer()).isSameAs(customer);
        Mockito.verify(customerService).increaseOrderCount(42L);
        Mockito.verify(orderRepository).save(order);
    }

    @Test
    void placeOrderForUnknownProduct_savesNothing() {
        // given
        Mockito.when(productService.getTotalPrice(Map.of("X-99", 1)))
                .thenThrow(new ProductNotFoundException("X-99"));

        // when
        Assertions.assertThatThrownBy(() -> orderService.placeOrder("+49 123 456789", Map.of("X-99", 1)))
                .isInstanceOf(ProductNotFoundException.class);

        // then -- der Versuch zählt, gespeichert wird nichts
        Mockito.verify(customerService).increaseOrderCount(42L);
        Mockito.verifyNoInteractions(orderRepository);
    }

    @Test
    void placeOrder_takesDeliveryTimeFromDeliveryTimeService() {
        // given -- the delivery time service answers 45 minutes for 40302
        Mockito.when(deliveryTimeClient.getDeliveryTimeInMinutes("40302")).thenReturn(45);
        Mockito.when(productService.getTotalPrice(Map.of("P-10", 1))).thenReturn(new BigDecimal("5.50"));
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order order = orderService.placeOrder("+49 123 456789", Map.of("P-10", 1));

        // then
        Assertions.assertThat(order.getEstimatedTimeOfDelivery())
                .isBetween(LocalDateTime.now().plusMinutes(44), LocalDateTime.now().plusMinutes(46));
    }

    @Test
    void placeOrder_fallsBackWhenDeliveryTimeServiceFails() {
        // given -- the delivery time service is unreachable
        Mockito.when(deliveryTimeClient.getDeliveryTimeInMinutes("40302"))
                .thenThrow(new ResourceAccessException("Connection refused"));
        Mockito.when(productService.getTotalPrice(Map.of("P-10", 1))).thenReturn(new BigDecimal("5.50"));
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Order order = orderService.placeOrder("+49 123 456789", Map.of("P-10", 1));

        // then -- 30 minutes from the OrderProperties
        Assertions.assertThat(order.getEstimatedTimeOfDelivery())
                .isBetween(LocalDateTime.now().plusMinutes(29), LocalDateTime.now().plusMinutes(31));
    }
}
