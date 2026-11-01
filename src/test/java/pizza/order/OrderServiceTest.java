package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ReflectionUtils;
import pizza.product.Product;
import pizza.product.ProductRepository;
import pizza.product.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// @SpringBootTest boots via SpringApplication so the DataLoadRunner runs and seeds the data;
// @ActiveProfiles("test") excludes the noisy LogicRunner (see @Profile("!test") on it).
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        // disable the discount so that the expected total price does not depend on the day (deterministic)
        "app.order.discount-days="
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    @DirtiesContext
    @org.junit.jupiter.api.Order(1)
    void placeOrder(@Autowired ProductService productService) {
        // Overwrite the productRepository instance in the productService with our dummy repository.
        // This is a bit of a hack (which will fail in later Java versions), but it works for testing purposes.
        ReflectionUtils.doWithFields(ProductService.class, field -> {
            if (field.getName().equals("productRepository")) {
                field.setAccessible(true);
                field.set(productService, new DummyProductRepository());
            }
        });

        // when -- the product prices come from the DummyProductRepository:
        //         2 * 8.00 (Margherita) + 1 * 10.00 (Salami) = 26.00, no discount (switched off via @TestPropertySource)
        Order order = orderService.placeOrder(
                "+49 123 456789",
                Map.of(
                        "PM", 2,
                        "PS", 1
                )
        );

        // then
        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.getId()).isEqualTo(1L);
        Assertions.assertThat(order.getTotalPrice()).isEqualByComparingTo(new BigDecimal("26.00"));

        // and: the OrderService holds exactly this one order
        Assertions.assertThat(orderService.getOrders()).hasSize(1);
    }


    /**
     * This test works with the original ProductRepository bean -- and fails, if the previous test hasn't cleaned up
     * its modified context.
     */
    @Test
    @org.junit.jupiter.api.Order(2)
    void placeAnotherOrder() {
        // when -- fails if DummyProductRepository is still installed
        Order order = orderService.placeOrder(
                "+49 123 456789",
                Map.of("S-01", 3)
        );
    }


    /**
     * A hand-written dummy {@link ProductRepository} with hard-wired products.
     * Replaces the real repository so that the {@code ProductService} returns known prices in the test
     * (mocking-like behavior without a mocking framework).
     */
    static class DummyProductRepository implements ProductRepository {

        private final Map<String, Product> products = Map.of(
                "PM", new Product("PM", "Fake Pizza Margherita", new BigDecimal("8.00")),
                "PS", new Product("PS", "Fake Pizza Salami", new BigDecimal("10.00"))
        );

        @Override
        public Product save(Product product) {
            throw new UnsupportedOperationException("Dummy repository is read-only");
        }

        @Override
        public boolean existsById(String productId) {
            return products.containsKey(productId);
        }

        @Override
        public List<Product> findAll() {
            return List.copyOf(products.values());
        }

        @Override
        public Optional<Product> findById(String productId) {
            return Optional.ofNullable(products.get(productId));
        }
    }
}
