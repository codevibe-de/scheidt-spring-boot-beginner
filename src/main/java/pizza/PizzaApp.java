package pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Spring Boot entry point.
 *
 * <p>{@code @SpringBootApplication} bundles {@code @Configuration},
 * {@code @ComponentScan} (rooted at this package) and {@code @EnableAutoConfiguration}.
 * The latter is what wires up the H2 {@code DataSource} and runs {@code schema.sql} —
 * infrastructure we previously declared by hand.
 *
 * <p>{@link SpringApplication#run} also invokes every {@link org.springframework.boot.CommandLineRunner}
 * bean after startup, so the old {@code @EventListener(ContextRefreshedEvent)} plumbing is gone.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class PizzaApp {

    public static void main(String[] args) {
        SpringApplication.run(PizzaApp.class, args);
    }

}
