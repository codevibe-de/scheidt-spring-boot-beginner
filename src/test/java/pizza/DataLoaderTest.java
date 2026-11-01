package pizza;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import pizza.customer.CustomerService;
import pizza.product.ProductService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// exercise c): every DataLoader is looked up by its bean name and run on its own
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "app.data-loader=none")
class DataLoaderTest {

    @Autowired
    Map<String, DataLoader> loaders;
    @Autowired
    ProductService productService;
    @Autowired
    CustomerService customerService;

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "none,   0, 0",
            "sample, 6, 2",
            "csv,    6, 0"
    })
    // without a new context "csv" fails on the products "sample" has already created
    @DirtiesContext
    void loaderCreatesData(String beanName, int products, int customers) {
        loaders.get(beanName).run();
        assertThat(productService.getAllProducts()).hasSize(products);
        assertThat(customerService.getAllCustomers()).hasSize(customers);
    }

    // bonus: notices when a new loader is missing from the @CsvSource
    @Test
    void allLoadersAreCovered() {
        assertThat(loaders).containsOnlyKeys("none", "sample", "csv");
    }
}
