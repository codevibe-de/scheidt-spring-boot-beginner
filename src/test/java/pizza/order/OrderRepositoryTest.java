package pizza.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import pizza.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Übung c): Slice nur mit JPA -- Flyway legt das Schema an, der DataLoader läuft nicht,
// jeder Test läuft in einer Transaktion und wird am Ende zurückgerollt
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = entityManager.persist(new Customer("Trans Action", null, "0931 123456"));
        entityManager.persist(new Order(customer, new BigDecimal("12.50"), LocalDateTime.now()));
        entityManager.persist(new Order(customer, new BigDecimal("7.40"), LocalDateTime.now()));
        // alles in die Datenbank schreiben und vergessen, damit die Abfragen wirklich lesen
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByCustomerPhoneNumber() {
        Assertions.assertThat(orderRepository.findByCustomerPhoneNumber("0931 123456"))
                .hasSize(2)
                .allSatisfy(order -> Assertions.assertThat(order.getCustomer()).isEqualTo(customer));
    }

    @Test
    void sumTotalPriceByPhoneNumber() {
        Assertions.assertThat(orderRepository.sumTotalPriceByPhoneNumber("0931 123456")).isEqualByComparingTo("19.90");
        Assertions.assertThat(orderRepository.sumTotalPriceByPhoneNumber("0000")).isEqualByComparingTo("0");
    }

    @Test
    void noSampleData() {
        // der DataLoader ist kein Teil des Slice
        Assertions.assertThat(orderRepository.count()).isEqualTo(2);
    }
}
