package pizza;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import pizza.order.Order;
import pizza.order.OrderService;

import java.util.Map;

// Bonus d): dieselben Migrationen und Abläufe gegen ein echtes PostgreSQL im Container;
// ohne Docker wird die Klasse übersprungen statt rot
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class PostgresIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    Flyway flyway;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderService orderService;

    @Test
    void migrationsApplied() {
        Assertions.assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("3");
        Assertions.assertThat(jdbcTemplate.queryForObject("SELECT version()", String.class)).startsWith("PostgreSQL 17");
    }

    @Test
    void placeOrder() {
        Order order = orderService.placeOrder("+49 123 456789", Map.of("P-10", 2));
        Assertions.assertThat(order.getId()).isNotNull();
    }
}
